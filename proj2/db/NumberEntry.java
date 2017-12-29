package db;

public class IntEntry implements Entry {

    private int value = 0;

    public IntEntry() {}

    public IntEntry(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Entry entry) {
        if (entry instanceof IntEntry) {
            return Integer.compare(value, ((IntEntry) entry).getValue());
        } else if (entry instanceof FloatEntry) {
            return Double.compare(value, ((FloatEntry) entry).getValue());
        } else {
            throw new RuntimeException("Cannot compare int to non-int or non-float.");
        }
    }

    @Override
    public String toString() {
        return String.format("", )
    }

    @Override
    public Operable add(Operable o) {
        return null;
    }

    @Override
    public Operable subtract(Operable o) {
        return null;
    }

    @Override
    public Operable multiply(Operable o) {
        return null;
    }

    @Override
    public Operable divide(Operable o) {
        return null;
    }
}
