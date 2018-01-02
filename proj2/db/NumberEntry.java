package db;

public class NumberEntry implements Entry {

    private static final String NOVALUE_REP = "NOVALUE";
    private static final double NOVALUE_VAL = 0.0;
    private static final String NAN_REP = "NaN";
    private static final double NAN_VAL = Double.POSITIVE_INFINITY;

    private double value = 0;
    private Type type = Type.FLOAT;
    private boolean nan = false;
    private boolean noValue = false;

    public NumberEntry() {}

    public NumberEntry(int value) {
        this(value, false);
    }

    public NumberEntry(double value) {
        this(value, true);
    }

    public NumberEntry(Type type) {
        this.type = type;
    }

    private NumberEntry(double value, boolean isFloat) {
        if (isFloat) {
            this.value = value;
            this.type = Type.FLOAT;
        } else {
            this.value = Math.round(value);
            this.type = Type.INT;
        }
    }

    @Override
    public NumberEntry copy() {
        NumberEntry e = new NumberEntry();
        e.value = value;
        e.type = type;
        e.nan = nan;
        e.noValue = noValue;
        return e;
    }

    @Override
    public Type getType() {
        return type;
    }

    private NumberEntry setNan(boolean nan) {
        this.nan = nan;
        if (nan) {
            this.value = NAN_VAL;
        }
        return this;
    }

    private NumberEntry setNoValue(boolean noValue) {
        this.noValue = noValue;
        if (noValue) {
            this.value = NOVALUE_VAL;
        }
        return this;
    }

    @Override
    public int compareTo(Entry entry) {
        // needs to handle no-value case: if any operand is no-value, comparison should fail.
        if (entry instanceof NumberEntry) {
            NumberEntry e = (NumberEntry) entry;
            // now all NaNs should have NAN_VAL which is maximum, so the special handling below is not necessary.
//            if (nan && e.nan) { // if both are nan, they are equal
//                return 0;
//            } else if (e.nan) {
//                return -1; // only entry is nan
//            } else if (nan){
//                return 1; // only this is nan
//            }
            return Double.compare(value, e.value);
        } else {
            throw new UnsupportedOperationException("Cannot compare int to non-int or non-float.");
        }
    }

    @Override
    public String toString() {
        // special cases: NaN and NOVALUE
        if (nan) {
            return NAN_REP;
        }
        if (noValue) {
            return NOVALUE_REP;
        }
        if (type == Type.INT) {
            return String.format("%.0f", value);
        } else if (type == Type.FLOAT) {
            return String.format("%.3f", value);
        } else {
            throw new RuntimeException("Illegal number type " + type + ".");
        }
    }

    private void resetProperties() {
        nan = false;
        noValue = false;
    }

    @Override
    public NumberEntry parse(String s) throws NumberFormatException {
        if (s != null) {
            if (s.equals(NOVALUE_REP)) {
                setNoValue(true);
            } else if (s.equals(NAN_REP)){
                setNan(true);
            } else {
                if (type == Type.FLOAT) {
                    value = Double.parseDouble(s);
                } else if (type == Type.INT) {
                    value = Integer.parseInt(s);
                } else {
                    throw new NumberFormatException("Number type mismatch: Parsing " + s + " with type " + type + ".");
                }
                resetProperties();
            }
            return this;
        }
        throw new NullPointerException("Attempt to parse null to number data entry.");
    }

    private static boolean isAnyFloat(NumberEntry a, NumberEntry b) {
        return a.type == Type.FLOAT || b.type == Type.FLOAT;
    }

    private static boolean isAnyNaN(NumberEntry a, NumberEntry b) {
        return a.nan || b.nan;
    }

    private static boolean isBothNoValue(NumberEntry a, NumberEntry b) {
        return a.noValue && b.noValue;
    }

    @Override
    public NumberEntry operate(String operator, Operable o) throws UnsupportedOperationException {
        if (o instanceof NumberEntry) {
            double newValue;
            boolean newNan = isAnyNaN(this, (NumberEntry) o);
            switch (operator) {
                case "+":
                    newValue = value + ((NumberEntry) o).value;
                    break;
                case "-":
                    newValue = value - ((NumberEntry) o).value;
                    break;
                case "*":
                    newValue = value * ((NumberEntry) o).value;
                    break;
                case "/":
                    newValue = value / ((NumberEntry) o).value;
                    // special case for division: if denominator is zero, result will be nan
                    newNan = Double.isInfinite(newValue) || newNan;
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported operator '" + operator + "' for numbers.");
            }

            return new NumberEntry(newValue, isAnyFloat(this, (NumberEntry) o))
                    .setNan(newNan)
                    .setNoValue(isBothNoValue(this, (NumberEntry) o));
        } else {
            throw new UnsupportedOperationException("Cannot perform '" + operator + "' between number " +
                    toString() + " and non-number " + o.toString() + ".");
        }
    }

    @Override
    public NumberEntry add(Operable o) throws UnsupportedOperationException {
        return operate("+", o);
//        if (o instanceof NumberEntry) {
//            return new NumberEntry(value + ((NumberEntry) o).value,
//                    isAnyFloat(this, (NumberEntry) o))
//                    .setNan(isAnyNaN(this, (NumberEntry) o))
//                    .setNoValue(isBothNoValue(this, (NumberEntry) o));
//        } else {
//            throw new UnsupportedOperationException("Cannot add number and non-number.");
//        }
    }

    @Override
    public NumberEntry subtract(Operable o) throws UnsupportedOperationException {
        return operate("-", o);
//        if (o instanceof NumberEntry) {
//            return new NumberEntry(value - ((NumberEntry) o).value,
//                    isAnyFloat(this, (NumberEntry) o))
//                    .setNan(isAnyNaN(this, (NumberEntry) o))
//                    .setNoValue(isBothNoValue(this, (NumberEntry) o));
//        } else {
//            throw new UnsupportedOperationException("Cannot subtract number and non-number.");
//        }
    }

    @Override
    public NumberEntry multiply(Operable o) throws UnsupportedOperationException {
        return operate("*", o);
//        if (o instanceof NumberEntry) {
//            return new NumberEntry(value * ((NumberEntry) o).value,
//                    isAnyFloat(this, (NumberEntry) o))
//                    .setNan(isAnyNaN(this, (NumberEntry) o))
//                    .setNoValue(isBothNoValue(this, (NumberEntry) o));
//        } else {
//            throw new UnsupportedOperationException("Cannot multiply number and non-number.");
//        }
    }

    @Override
    public NumberEntry divide(Operable o) throws UnsupportedOperationException {
        return operate("/", o);
//        // need to check for zero denominator here
//        // if denominator is zero, result will be nan
//        if (o instanceof NumberEntry) {
//            NumberEntry e = (NumberEntry) o;
//            double v = value / ((NumberEntry) o).value;
//            boolean nan = Double.isInfinite(v) || isAnyNaN(this, e);
//            return new NumberEntry(v, isAnyFloat(this, e)).setNan(nan)
//                    .setNoValue(isBothNoValue(this, (NumberEntry) o));
//        } else {
//            throw new UnsupportedOperationException("Cannot divide number and non-number.");
//        }
    }
}
