package db;

/**
 * A column expression is an expression of the form
 * <operand0> <arithmetic operator> <operand1> as <column alias>,
 * or it may just be a single operand.
 *
 * Valid operands are column names and literals.
 *
 * Valid arithmetic operators are +, -, *, and / for int and float types.
 * For strings, the only allowed operation is concatenation,
 * which is represented by the + operator.
 *
 * There are a few special cases for column expressions, listed below.
 *
 * 1. If a lone * is supplied instead of a list of column expressions,
 *    all columns of the result of the joinWith should be selected.
 * 2. If only a single operand is given, it must be a column name.
 *    The new column shares its name with the original column.
 * 3. If two operands are given, the left must be a column name while
 *    the right could be either a column name or a literal.
 *    An alias must always be provided when there are two operands.
 */
public interface ColumnExpr {
    /**
     * Returns a column from the given table that evaluates to this expression
     * @param table
     * @return
     */
    Column evaluate(Table table);
}
