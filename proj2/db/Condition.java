package db;

/**
 * A condition statement is a comparison of rows in the given tables.
 *
 * There are two kinds of conditions: unary and binary.
 * Unary conditions are of the form <column name> <comparison> <literal>,
 * while binary conditions are of the form <column0 name> <comparison> <column1 name>.
 * The difference is that unary conditions involve only one column,
 * while binary conditions involve two columns. You may assume that in a unary condition,
 * the literal is always the right operand.
 *
 * Valid conditional comparators are ==, !=, <, >, <= and >=.
 */
public interface Condition {
    boolean isSatisfied(Row row);
}
