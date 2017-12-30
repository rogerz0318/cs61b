package db;

public class StringEntry implements Entry {

    private String value = "";

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
        if (value == null || value.equals("")) {
            return "NOVALUE";
        }
        return "'" + value + "'";
    }

    @Override
    public StringEntry parse(String s) {
        if (s != null && !s.equals("")) {
            value = s;
        }
        return this;
    }

    @Override
    public StringEntry add(Operable o) {
        if (!(o instanceof StringEntry)) {
            throw new RuntimeException("Cannot add string and non-string.");
        } else {
            return new StringEntry(value + ((StringEntry) o).value);
        }
    }

    @Override
    public StringEntry subtract(Operable o) {
        throw new RuntimeException("Cannot subtract string.");
    }

    @Override
    public StringEntry multiply(Operable o) {
        throw new RuntimeException("Cannot multiply string.");
    }

    @Override
    public StringEntry divide(Operable o) {
        throw new RuntimeException("Cannot divide string.");
    }
}
