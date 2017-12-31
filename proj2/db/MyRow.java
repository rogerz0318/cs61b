package db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRow implements Row {

    private static String delimiter = ",";
    private List<Entry> entries;

    /**
     * Initialize a row with given type data entries.
     * Relies on StringEntry and NumberEntry classes.
     * @param types
     * @see StringEntry
     * @see NumberEntry
     */
    public MyRow(List<Type> types) {
        this.entries = new ArrayList<>(types.size());
        for (Type type : types) {
            if (type == Type.STRING) {
                entries.add(new StringEntry());
            } else if (type == Type.INT || type == Type.FLOAT) {
                entries.add(new NumberEntry(type));
            } else {
                throw new UnsupportedOperationException("Unsupported type " + type + ".");
            }
        }
    }

    public MyRow(Entry[] entries) {
        this.entries = new ArrayList<>();
        this.entries.addAll(Arrays.asList(entries));
    }

    private MyRow() {}

    @Override
    public MyRow copy() {
        MyRow r = new MyRow();
        r.entries = new ArrayList<>();
        for (Entry e: entries) {
            r.entries.add(e.copy());
        }
        return r;
    }

    public static void setDelimiter(String s) {
        delimiter = s;
    }

    // Deprecated. Type check is better to be handled by Table class.
//    public MyRow(List<Entry> entries, List<Type> types) {
//        this.entries = new ArrayList<>(types.size());
//        for (int i = 0; i < types.size(); i++) {
//            Type type = types.getData(i);
//            Entry entry = entries.getData(i);
//            if (type == entry.getType()) {
//                entries.add(entry);
//            } else {
//                throw new RuntimeException("Data entry type " +
//                        entry.getType() + "does not match column type" +
//                        type + ".");
//            }
//        }
//    }

    @Override
    public List<Entry> getAllData() {
        return entries;
    }

    @Override
    public MyRow parse(String s) {
        String[] tokens = s.split(delimiter);
        for (int i = 0; i < tokens.length && i < entries.size(); i++) {
            entries.get(i).parse(tokens[i]);
        }
        return this;
    }

    @Override
    public String toString() {
        String s = "";
        for (Entry e : entries) {
            s += delimiter + e.toString();
        }
        return s.substring(delimiter.length()); // remove first delimiter
    }
}
