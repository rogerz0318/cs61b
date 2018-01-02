package db;

public class MyColumnExpr implements ColumnExpr {

    private String operand1;
    private String operand2;
    private String operator;
    private String alias;
    private boolean unary;

    public MyColumnExpr(String operand1) {
        // unary column expression
        this(operand1, null, null, operand1, true);
    }

    public MyColumnExpr(String operand1, String operator, String operand2) {
        // binary column expression, default alias is the same as the operand1 column name
        this(operand1, operator, operand2, operand1, false);
    }

    public MyColumnExpr(String operand1, String operator, String operand2, String alias) {
        // binary column expression, alias is specified
        this(operand1, operator, operand2, alias, false);
    }

    private MyColumnExpr(String operand1, String operator, String operand2, String alias, boolean unary) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operator = operator;
        this.alias = alias;
        this.unary = unary;
    }

    @Override
    public Column evaluate(Table table) {
        // first operand must be a column
        Column o1 = table.getColumnByName(operand1);
        if (o1 == null) {
            throw new RuntimeException("Cannot find column named " + operand1 + " in table " + table.getName() + ".");
        }
        // unary case (only one operator, excluding the case of * which needs to be handled in upper level)
        if (unary) {
            return o1;
        }
        // binary case: operand2 is either column or entry
        Operable o2 = table.getColumnByName(operand2);
        if (o2 == null) {
            // o2 could be a data entry, try parse it
            try {
                // is it an integer?
                o2 = new NumberEntry(Type.INT).parse(operand2);
            } catch (NumberFormatException e0) {
                try {
                    // or is it a float?
                    o2 = new NumberEntry(Type.FLOAT).parse(operand2);
                } catch (NumberFormatException e1) {
                    // if not, it must be a string then
                    try {
                        o2 = new StringEntry().parse(operand2);
                    } catch (IllegalArgumentException e2) {
                        // if none of the above
                        throw new IllegalArgumentException(operand2 + " is neither a column name, " +
                                "or parsable into any data entry.");
                    }
                }
            }
        }

        // evaluate the expression
        Column out = (Column) o1.operate(operator, o2);
        out.setName(alias);
        return out;
    }
}
