package db;

public class StringEntry implements Entry {

    private String value = "";
    private Type type = Type.STRING;

    public StringEntry() {}

    public StringEntry(String value) {
        if (value == null) {
            throw new RuntimeException("String cannot be initialized with null.");
        }
        this.value = value;
    }

//    public String getValue() {
//        return value;
//    }

    @Override
    public int compareTo(Entry entry) {
        if (!(entry instanceof StringEntry)) {
            throw new RuntimeException("Cannot compare string to non-string.");
        }
        return value.compareTo(((StringEntry) entry).value);
    }

    @Override
    public String toString() {
        if (value == null || value == "") {
            return "NOVALUE";
        }
        return "'" + value + "'";
    }

    @Override
    public Operable add(Operable o) {
        if (!(o instanceof StringEntry)) {
            throw new RuntimeException("Cannot add string and non-string.");
        } else {
            return new StringEntry(value + ((StringEntry) o).value);
        }
    }

    @Override
    public Operable subtract(Operable o) {
        throw new RuntimeException("Cannot subtract string.");
    }

    @Override
    public Operable multiply(Operable o) {
        throw new RuntimeException("Cannot multiply string.");
    }

    @Override
    public Operable divide(Operable o) {
        throw new RuntimeException("Cannot divide string.");
    }
}
