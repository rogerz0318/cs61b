package db;

import java.util.ArrayList;
import java.util.List;

public class MyRow implements Row {

    public static final String DELIMITER = ",";
    private List<Entry> entries;

    private MyRow() {} // prohibit empty constructor

    public MyRow(List<Type> types) {
        this(new String[types.size()], types);
    }

    public MyRow(String[] values, List<Type> types) {
        this.entries = new ArrayList<>(types.size());
        for (int i = 0; i < types.size(); i++) {
            Type type = types.get(i);
            if (type == Type.STRING) {
                entries.add(new StringEntry().parse(values[i]));
            } else if (type == Type.INT || type == Type.FLOAT) {
                entries.add(new NumberEntry(type).parse(values[i]));
            } else {
                throw new RuntimeException("Illegal number type.");
            }
        }
    }

    @Override
    public Entry get(int index) {
        return entries.get(index);
    }

    @Override
    public String toString() {
        String s = "";
        for (Entry e : entries) {
            s += DELIMITER + e.toString();
        }
        return s.substring(1); // remove first delimiter
    }
}
