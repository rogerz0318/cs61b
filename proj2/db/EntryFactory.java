package db;

public class EntryFactory {

    /**
     * Returns a correct type of Entry instance that corresponds to the given string input.
     * @param s
     * @return
     */
    public static Entry parse(String s) {
        Entry e;
        try {
            // is it an integer?
            e = new NumberEntry(Type.INT).parse(s);
        } catch (NumberFormatException e0) {
            try {
                // or is it a float?
                e = new NumberEntry(Type.FLOAT).parse(s);
            } catch (NumberFormatException e1) {
                // if not, it must be a string then
                try {
                    e = new StringEntry().parse(s);
                } catch (IllegalArgumentException e2) {
                    // if none of the above
                    throw new IllegalArgumentException(s + " cannot be parsed into any data entry.");
                }
            }
        }
        return e;
    }
}
