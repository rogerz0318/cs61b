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
 */
public interface Operable {

    /**
     * Performs addition (concatenation for strings)
     * @param o
     * @return
     */
    Operable add(Operable o);

    /**
     * Performs subtraction (throws RuntimeException for strings)
     * @param o
     * @return
     */
    Operable subtract(Operable o);

    /**
     * Performs multiplication (throws RuntimeException for strings)
     * @param o
     * @return
     */
    Operable multiply(Operable o);

    /**
     * Performs division (throws RuntimeException for strings).
     * If divided by zero, returns NaN.
     * @param o
     * @return
     */
    Operable divide(Operable o);
}
