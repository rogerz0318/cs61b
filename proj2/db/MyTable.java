package db;

import java.io.*;
import java.util.*;

public class MyTable implements Table {

    private static String delimiter = ",";
    private String name;
    private List<Row> rows;
    private List<Type> columnTypes;
    private List<String> columnNames;

    /**
     * Takes a column title, parse into column names and types. The column title should take this form: "UserID
     * string,Lastname string,Age int", with column name and type separated by a space, and titles separated by
     * delimiters.
     *
     * @param columnTitle
     */
    public MyTable(String name, String columnTitle) {
        this.name = name;
        columnTypes = new ArrayList<>();
        columnNames = new ArrayList<>();
        for (String title : columnTitle.split(delimiter)) {
            int p = title.lastIndexOf(" "); // look for last occurrence of space
            columnNames.add(title.substring(0, p)); // first part is name
            columnTypes.add(Type.valueOf(title.substring(p + 1).toUpperCase())); // last part is type
        }
        rows = new ArrayList<>();
    }

    public static MyTable createFromFile(File file) throws IOException {
        MyTable t = null;
        String line = null;
        // Get the file name without extension as the table name.
        String fileName = file.getName();
        int dot = fileName.lastIndexOf(".");
        String tableName = fileName.substring(0, dot);
        // Read file and parse line
        BufferedReader br = new BufferedReader(new FileReader(file));
        line = br.readLine();
        t = new MyTable(tableName, line);
        while ((line = br.readLine()) != null) {
            t.insertRow(new MyRow(t.columnTypes).parse(line));
        }
        br.close();
        return t;
    }

    private MyTable() {}

    public static void setDelimiter(String s) {
        delimiter = s;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAllColumnNames() {
        return columnNames;
    }

    @Override
    public List<Type> getAllColumnTypes() {
        return columnTypes;
    }

    @Override
    public List<Row> getAllRows() {
        return rows;
    }

    private MyTable doCartesianProduct(String name, Table table) {
        List<String> joinedNames = new ArrayList<>();
        List<Type> joinedTypes = new ArrayList<>();
        List<Row> joinedRows = new ArrayList<>();

        // String and Type are immutable, so safe to directly add to lists
        joinedNames.addAll(columnNames);
        joinedNames.addAll(table.getAllColumnNames());
        joinedTypes.addAll(columnTypes);
        joinedTypes.addAll(table.getAllColumnTypes());

        // Rows are mutable (at least for MyRow), so need to make copies
        for (Row r1 : rows) {
            for (Row r2 : table.getAllRows()) {
                List<Entry> merged = new ArrayList<>();
                merged.addAll(r1.copy().getAllData());
                merged.addAll(r2.copy().getAllData());
                joinedRows.add(new MyRow(merged.toArray(new Entry[0])));
            }
        }

        // Make new table
        MyTable returnVal = new MyTable();
        returnVal.name = name;
        returnVal.columnNames = joinedNames;
        returnVal.columnTypes = joinedTypes;
        returnVal.rows = joinedRows;

        return returnVal;
    }

    /**
     * Performs natural inner join with another table. In a natural inner join, the new table's rows are formed by
     * merging pairs of rows from the input tables. Two rows should be merged if and only if all of their shared columns
     * have the same values. In the case that the input tables have no columns in common, the resulting table is what is
     * called the Cartesian Product of the tables. That is, each row of table A is considered to match each row of table
     * B as if they had a column in common.
     *
     * @param table
     * @return
     */
    @Override
    public MyTable joinWith(String name, Table table) {
        // find the shared columns
        List<String> sharedColumnNames = new ArrayList<>();
        List<Type> sharedColumnTypes = new ArrayList<>();
        Map<Integer, Integer> sharedIndex = new LinkedHashMap<>();

        for (int i = 0; i < columnNames.size(); i++) {
            String name1 = columnNames.get(i);
            for (int j = 0; j < table.getAllColumnNames().size(); j++) {
                String name2 = table.getAllColumnNames().get(j);
                if (name1.equals(name2)) { // potentially need to check if type matches as well
                    sharedColumnNames.add(name1);
                    sharedColumnTypes.add(columnTypes.get(i)); // just store this table's column type if match
                    // i, j corresponds to two data entries that belongs to the same shared column
                    sharedIndex.put(i, j);
                }
            }
        }

        if (sharedIndex.isEmpty()) {
            return doCartesianProduct(name, table);
        } else {
            // otherwise do inner join
            List<Row> joinedRows = new ArrayList<>();
            for (Row r1 : rows) {
                for (Row r2 : table.getAllRows()) {
                    boolean match = true;
                    for (int i : sharedIndex.keySet()) {
                        int j = sharedIndex.get(i);
                        boolean dataMatches = r1.getData(i).compareTo(r2.getData(j)) == 0;
                        match = match && dataMatches;
                    }
                    if (match) {
                        // merge the matched row
                        List<Entry> merged = new ArrayList<>(); // store entries after merge
                        List<Entry> copyOfRow1 = r1.copy().getAllData();
                        List<Entry> copyOfRow2 = r2.copy().getAllData();
                        for (int k : sharedIndex.keySet()) {
                            merged.add(copyOfRow1.get(k)); // add the overlapped data in order of the first row
                        }
                        for (int i = 0; i < copyOfRow1.size(); i++) {
                            if (!sharedIndex.keySet().contains(i)) {
                                merged.add(copyOfRow1.get(i)); // add from row 1, excluding the matched entries
                            }
                        }
                        for (int j = 0; j < copyOfRow2.size(); j++) {
                            if (!sharedIndex.values().contains(j)) {
                                merged.add(copyOfRow2.get(j)); // add from row 2, excluding the matched entries
                            }
                        }
                        joinedRows.add(new MyRow(merged.toArray(new Entry[0])));
                    }
                }
            }

            // make all joined table instance variables
            List<String> names1 = columnNames;
            List<String> names2 = table.getAllColumnNames();
            List<Type> types1 = columnTypes;
            List<Type> types2 = table.getAllColumnTypes();

            List<String> joinedNames = new ArrayList<>();
            List<Type> joinedTypes = new ArrayList<>();

            // add the overlapped names in order of the first row
            joinedNames.addAll(sharedColumnNames);
            joinedTypes.addAll(sharedColumnTypes);
            for (int i = 0; i < names1.size(); i++) {
                if (!sharedIndex.keySet().contains(i)) {
                    // add from this table, excluding the matched columns
                    joinedNames.add(names1.get(i));
                    joinedTypes.add(types1.get(i));
                }
            }
            for (int j = 0; j < names2.size(); j++) {
                if (!sharedIndex.values().contains(j)) {
                    // add from row 2, excluding the matched entries
                    joinedNames.add(names2.get(j));
                    joinedTypes.add(types2.get(j));
                }
            }

            // make table with merged instance variables
            MyTable returnVal = new MyTable();
            returnVal.name = name;
            returnVal.columnNames = joinedNames;
            returnVal.columnTypes = joinedTypes;
            returnVal.rows = joinedRows;

            return returnVal;
        }
    }

    @Override
    public void writeToFile(File file) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(titleRow());
        for (Row r : rows) {
            bw.newLine();
            bw.write(r.toString());
        }
        bw.close();
    }

    private boolean isTypeCompatible(Row row) {
        if (row.getAllData().size() != columnTypes.size()) {
            return false;
        }
        boolean compatible = true;
        for (int i = 0; i < columnTypes.size(); i++) {
            compatible = compatible && row.getData(i).getType() == columnTypes.get(i);
        }
        return compatible;
    }

    @Override
    public void insertRow(Row row) {
        if (isTypeCompatible(row)) {
            rows.add(row);
        } else {
            throw new IllegalArgumentException("Cannot insertRow row " + row + " into table, type is incompatible.");
        }
    }

    @Override
    public Table filterBy(Condition c) {
        return null;
    }

    @Override
    public Table selectBy(ColumnExpression ce) {
        return null;
    }

    private String titleRow() {
        String s = "";
        for (int i = 0; i < columnTypes.size(); i++) {
            s += delimiter + columnNames.get(i) + " " + columnTypes.get(i).name().toLowerCase();
        }
        return s.substring(delimiter.length());
    }

    @Override
    public String toString() {
        String s = titleRow();
        for (Row r : rows) {
            s += "\n" + r.toString();
        }
        return s;
    }

    public static void main(String[] args) throws Exception {
        MyTable t1 = MyTable.createFromFile(new File("examples/t1.tbl"));
        MyTable t2 = MyTable.createFromFile(new File("examples/t2.tbl"));
        MyTable t3 = t1.joinWith("t3", t2);
        System.out.println(t3);
        MyTable t4 = MyTable.createFromFile(new File("examples/t4.tbl"));
        MyTable t5 = t3.joinWith("t5", t4);
        System.out.println(t5);
    }
}
