package db;

public class UnaryColumnExpr implements ColumnExpr {

    private String operand;

    public UnaryColumnExpr(String operand) {
        // unary column expression
        this.operand = operand;
    }

    @Override
    public Column evaluate(Table table) {
        // first operand must be a column
        Column c = table.getColumnByName(operand);
        if (c == null) {
            throw new RuntimeException("Cannot find column named '" + operand + "' in table '" + table.getName() + "'.");
        }
        // unary case (only one operator, excluding the case of * which needs to be handled in upper level)
        return c;
    }
}
