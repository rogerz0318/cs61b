package db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
                                COMMA = "\\s*,\\s*",
                                AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
                                 LOAD_CMD   = Pattern.compile("load " + REST),
                                 STORE_CMD  = Pattern.compile("store " + REST),
                                 DROP_CMD   = Pattern.compile("drop table " + REST),
                                 INSERT_CMD = Pattern.compile("insert into " + REST),
                                 PRINT_CMD  = Pattern.compile("print " + REST),
                                 SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
                                               "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
                                 SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                                               "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                                               "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                                               "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
                                 CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                                                   SELECT_CLS.pattern()),
                                 INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                                               "\\s*(?:,\\s*.+?\\s*)*)");

    private Database db;

    public Parser(Database db) {
        this.db = db;
    }

    public String eval(String query) {
        try {
            Matcher m;
            if ((m = CREATE_CMD.matcher(query)).matches()) {
                createTable(m.group(1));
            } else if ((m = LOAD_CMD.matcher(query)).matches()) {
                loadTable(m.group(1));
            } else if ((m = STORE_CMD.matcher(query)).matches()) {
                storeTable(m.group(1));
            } else if ((m = DROP_CMD.matcher(query)).matches()) {
                dropTable(m.group(1));
            } else if ((m = INSERT_CMD.matcher(query)).matches()) {
                insertRow(m.group(1));
            } else if ((m = PRINT_CMD.matcher(query)).matches()) {
                return printTable(m.group(1));
            } else if ((m = SELECT_CMD.matcher(query)).matches()) {
                return select(m.group(1)).toString();
            } else {
                return String.format("ERROR: Malformed query: %s\n", query);
            }
            return "";
        } catch (Exception e) {
//            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    private void createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            throw new RuntimeException(String.format("Malformed create: %s\n", expr));
        }
    }

    private void createNewTable(String name, String[] cols) {
//        StringJoiner joiner = new StringJoiner(", ");
//        for (int i = 0; i < cols.length-1; i++) {
//            joiner.addTable(cols[i]);
//        }
//
//        String colSentence = joiner.toString() + " and " + cols[cols.length-1];
//        System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);
        db.addTable(new MyTable(name, cols));
    }

    private void createSelectedTable(String name, String exprs, String tables, String conds) {
//        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
//                " '%s' from the joinWith of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        Table t = select(exprs, tables, conds);
        t.setName(name);
        db.addTable(t);
    }

    private void loadTable(String name) throws IOException {
//        System.out.printf("You are trying to loadTableFromFile the table named %s\n", name);
        db.loadTableFromFile(new File(name + ".tbl"));
    }

    private void storeTable(String name) throws IOException {
//        System.out.printf("You are trying to writeToFile the table named %s\n", name);
        db.getTableByName(name).writeToFile(new File(name + ".tbl"));
    }

    private void dropTable(String name) {
//        System.out.printf("You are trying to drop the table named %s\n", name);
        db.dropTableByName(name);
    }

    private void insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            throw new RuntimeException(String.format("Malformed insertRow: %s\n", expr));
        }

//        System.out.printf("You are trying to insertRow the row \"%s\" into the table %s\n", m.group(2), m.group(1));
        String name = m.group(1);
        String content = m.group(2);
        Table table = db.getTableByName(name);
        MyRow newRow = new MyRow(table.getAllColumnTypes());
        newRow.parse(content);
        table.insertRow(newRow);
    }

    private String printTable(String name) {
//        System.out.printf("You are trying to print the table named %s\n", name);
        return db.getTableByName(name).toString();
    }

    private Table select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            throw new RuntimeException(String.format("Malformed selectBy: %s\n", expr));
        }

        return select(m.group(1), m.group(2), m.group(3));
    }

    private Table select(String exprs, String tables, String conds) {
//        System.out.printf("You are trying to selectBy these expressions:" +
//                " '%s' from the joinWith of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
        String[] nameArr = tables.split(COMMA);
        String[] exprArr = exprs.split(COMMA);

        // join tables
        Table t = db.joinTables(nameArr);

        // apply column expressions
        List<ColumnExpr> exprList = new ArrayList<>();
        Pattern exprPattern = Pattern.compile("\\s*(\\S+)(?:\\s*(\\W)\\s*(\\S+)\\s+as\\s+(\\S+))?\\s*");
        for (String expr : exprArr) {
            if (expr.equals("*")) { // wildcard
                if (exprArr.length != 1) { // but there are other expressions, then it is illegal
                    throw new RuntimeException("'*' can only be used without presence of other column expressions.");
                }
                break; // break the loop, Table t remains the same (all columns are included)
            } else {
                // parse expression, put in the list
                Matcher m = exprPattern.matcher(expr);
                if (m.matches()) {
                    // either one or four matched segments
                    if (m.group(1) != null && m.group(2) == null && m.group(3) == null && m.group(4) == null) {
                        // single operand
                        exprList.add(new UnaryColumnExpr(m.group(1)));
                    } else if (m.group(1) != null && m.group(2) != null && m.group(3) != null && m.group(4) != null) {
                        // two operands with alias (alias must be present in this case)
                        exprList.add(new BinaryColumnExpr(m.group(1), m.group(2), m.group(3), m.group(4)));
                    } else {
                        throw new RuntimeException("Illegal column expression: '" + expr + "'.");
                    }
                } else {
                    throw new RuntimeException("Illegal column expression: '" + expr + "'.");
                }
            }
        }
        if (!exprList.isEmpty()) {
            t = t.selectBy(exprList);
        }

        // apply conditions (optional)
        if (conds != null) {
            String[] condArr = conds.split(AND);
            Pattern condPattern = Pattern.compile("\\s*(\\S+)(?:\\s*([\\W&&\\S]+)\\s*(\\S+))?\\s*");
            for (String cond : condArr) { // this includes the case when condArr has zero length
                Matcher m = condPattern.matcher(cond);
                if (m.matches()) {
                    t = t.filterBy(new StdCondition(m.group(1), m.group(2), m.group(3), t));
                } else {
                    throw new RuntimeException("Illegal condition: '" + cond + "'.");
                }
            }
        }

        return t;
    }

    public static void main(String[] args) {
        Pattern exprPattern = Pattern.compile("\\s*(\\S+)(?:\\s*(\\W)\\s*(\\S+))?\\s*");
        Matcher m = exprPattern.matcher("xxx > 555");
        if (m.matches()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                System.out.println(m.group(i));
            }
        }
    }
}
