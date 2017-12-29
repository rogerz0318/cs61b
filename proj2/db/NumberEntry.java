package db;

public class NumberEntry implements Entry {

    private double value = 0;
    private Type type;
    private boolean infinite = false;

    public NumberEntry(int value) {
        this(value, false);
    }

    public NumberEntry(double value) {
        this(value, true);
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

//    public double getValue() {
//        return value;
//    }
//
//    public Type getType() {
//        return type;
//    }
//
//    public boolean isInfinite() {
//        return infinite;
//    }

    private NumberEntry setInfinite(boolean infinite) {
        this.infinite = infinite;
        return this;
    }

    @Override
    public int compareTo(Entry entry) {
        if (entry instanceof NumberEntry) {
            NumberEntry e = (NumberEntry) entry;
            if (infinite && e.infinite) { // if both are infinite, they are equal
                return 0;
            } else if (e.infinite) {
                return -1; // only entry is infinite
            } else if (infinite){
                return 1; // only this is infinite
            }
            return Double.compare(value, e.value);
        } else {
            throw new RuntimeException("Cannot compare int to non-int or non-float.");
        }
    }

    @Override
    public String toString() {
        if (infinite) {
            return "NaN";
        }
        if (type == Type.INT) {
            return String.format("%.0f", value);
        } else if (type == Type.FLOAT) {
            return String.format("%.3f", value);
        } else {
            throw new RuntimeException("Illegal number type.");
        }
    }

    private boolean isAnyFloat(NumberEntry a, NumberEntry b) {
        return a.type == Type.FLOAT || b.type == Type.FLOAT;
    }

    private boolean isAnyInfinite(NumberEntry a, NumberEntry b) {
        return a.infinite || b.infinite;
    }

    @Override
    public Operable add(Operable o) {
        if (o instanceof NumberEntry) {
            return new NumberEntry(value + ((NumberEntry) o).value,
                    isAnyFloat(this, (NumberEntry) o))
                    .setInfinite(isAnyInfinite(this, (NumberEntry) o));
        } else {
            throw new RuntimeException("Cannot add number and non-number.");
        }
    }

    @Override
    public Operable subtract(Operable o) {
        if (o instanceof NumberEntry) {
            return new NumberEntry(value - ((NumberEntry) o).value,
                    isAnyFloat(this, (NumberEntry) o))
                    .setInfinite(isAnyInfinite(this, (NumberEntry) o));
        } else {
            throw new RuntimeException("Cannot subtract number and non-number.");
        }
    }

    @Override
    public Operable multiply(Operable o) {
        if (o instanceof NumberEntry) {
            return new NumberEntry(value * ((NumberEntry) o).value,
                    isAnyFloat(this, (NumberEntry) o))
                    .setInfinite(isAnyInfinite(this, (NumberEntry) o));
        } else {
            throw new RuntimeException("Cannot multiply number and non-number.");
        }
    }

    @Override
    public Operable divide(Operable o) {
        // need to check for zero denominator here
        // if denominator is zero, result will be infinite
        if (o instanceof NumberEntry) {
            NumberEntry e = (NumberEntry) o;
            double v = value / ((NumberEntry) o).value;
            boolean inf = Double.isInfinite(v) || isAnyInfinite(this, e);
            return new NumberEntry(v, isAnyFloat(this, e)).setInfinite(inf);
        } else {
            throw new RuntimeException("Cannot add number and non-number.");
        }
    }
}
