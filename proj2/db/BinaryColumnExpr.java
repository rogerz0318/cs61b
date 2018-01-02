package db;

public class BinaryColumnExpr implements ColumnExpr {

    private String operand1;
    private String operand2;
    private String operator;
    private String alias;

    public BinaryColumnExpr(String operand1, String operator, String operand2, String alias) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operator = operator;
        this.alias = alias;
    }

    @Override
    public Column evaluate(Table table) {
        // first operand must be a column
        Column o1 = table.getColumnByName(operand1);
        if (o1 == null) {
            throw new RuntimeException("Cannot find column named " + operand1 + " in table '" + table.getName() + "'.");
        }
        // binary case: operand2 is either column or entry
        Operable o2 = table.getColumnByName(operand2);
        if (o2 == null) {
            // o2 could be a data entry, try parse it
            try {
                o2 = EntryFactory.parse(operand2);
            } catch (Exception e) {
                throw new RuntimeException(operand2 + " is neither a column name nor a parsable string ("
                        + e.getMessage() + ").");
            }
        }

        // evaluate the expression
        Column out = (Column) o1.operate(operator, o2);
        out.setName(alias);
        return out;
    }
}
