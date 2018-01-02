package db;

/**
 * An Operable should be able to perform addition, subtraction,
 * multiplication and division operations between them, depending
 * on specific implementation.
 *
 * For example, data columns and entries can be both Operable.
 * Adding columns means adding each entry, while adding column to
 * a data entry could mean adding all data in this column by that
 * given data entry.
 *
 * The arithmetic operations must return a new entry instance.
 * Operands may not be mutated.
 */
public interface Operable {
    /**
     * A general operation method that performs the supported operations
     * via a String operator (such as +, -, etc.). Returns the result Operable.
     * @param operator
     * @param o the operand
     * @return
     */
    Operable operate(String operator, Operable o) throws UnsupportedOperationException;

    /**
     * Performs addition (concatenation for strings)
     * @param o
     * @return
     * @throws UnsupportedOperationException
     */
    Operable add(Operable o) throws UnsupportedOperationException;

    /**
     * Performs subtraction (throws UnsupportedOperationException for strings)
     * @param o
     * @return
     * @throws UnsupportedOperationException
     */
    Operable subtract(Operable o) throws UnsupportedOperationException;

    /**
     * Performs multiplication (throws UnsupportedOperationException for strings)
     * @param o
     * @return
     * @throws UnsupportedOperationException
     */
    Operable multiply(Operable o) throws UnsupportedOperationException;

    /**
     * Performs division (throws UnsupportedOperationException for strings).
     * If divided by zero, returns NaN.
     * @param o
     * @return
     * @throws UnsupportedOperationException
     */
    Operable divide(Operable o) throws UnsupportedOperationException;
}
