package db;

public class StringEntry implements Entry {

    private static final String NOVALUE_REP = "NOVALUE";
    private static final String NOVALUE_VAL = "";
    private static final String NAN_REP = "NaN";

    private String value = "";
    private boolean noValue = false;

    public StringEntry() {}

    public StringEntry(String value) {
        if (value == null) {
            throw new IllegalArgumentException("String cannot be initialized with null.");
        }
        this.value = value;
    }

    private StringEntry setNoValue(boolean noValue) {
        this.noValue = noValue;
        if (noValue) {
            this.value = NOVALUE_VAL;
        }
        return this;
    }

    @Override
    public StringEntry copy() {
        StringEntry e = new StringEntry();
        e.value = value;
        e.noValue = noValue;
        return e;
    }

    @Override
    public Type getType() {
        return Type.STRING;
    }

    @Override
    public int compareTo(Entry entry) {
        // needs to handle no-value case: if any operand is no-value, comparison should fail.
        if (!(entry instanceof StringEntry)) {
            throw new UnsupportedOperationException("Cannot compare string to non-string.");
        } else {
            return value.compareTo(((StringEntry) entry).value);
        }
    }

    @Override
    public String toString() {
        // special case: NOVALUE
        if (value == null || noValue) {
            return NOVALUE_REP;
        }
        return "'" + value + "'";
    }

    @Override
    public StringEntry parse(String s) throws IllegalArgumentException {
        if (s != null) {
            if (s.startsWith("'") && s.endsWith("'") && !s.equals(NAN_REP)) {
                value = s.substring(1, s.length() - 1);
                noValue = false;
            } else if (s.equals(NOVALUE_REP)) {
                // special case: NOVALUE
                setNoValue(true);
            } else {
                throw new IllegalArgumentException("Cannot parse " + s + " as string data entry.");
            }
            return this;
        }
        throw new NullPointerException("Attempt to parse null to string data entry.");
    }

    @Override
    public StringEntry operate(String operator, Operable o) throws UnsupportedOperationException {
        if (o instanceof StringEntry) {
            if (operator.equals("+")) {
                StringEntry e = (StringEntry) o;
                // if both are no-value, result should be no-value
                return new StringEntry(value + e.value).setNoValue(noValue && e.noValue);
            } else {
                throw new UnsupportedOperationException("Unsupported operator '" + operator + "' for strings.");
            }
        } else {
            throw new UnsupportedOperationException("Cannot perform '" + operator + "' between string " +
                    toString() + " and non-string " + o.toString() + ".");
        }
    }

    @Override
    public StringEntry add(Operable o) throws UnsupportedOperationException {
        return operate("+", o);
    }

    @Override
    public StringEntry subtract(Operable o) throws UnsupportedOperationException {
        return operate("-", o);
    }

    @Override
    public StringEntry multiply(Operable o) throws UnsupportedOperationException {
        return operate("*", o);
    }

    @Override
    public StringEntry divide(Operable o) throws UnsupportedOperationException {
        return operate("/", o);
    }
}
