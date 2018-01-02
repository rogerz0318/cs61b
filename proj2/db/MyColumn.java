package db;

import java.util.ArrayList;
import java.util.List;

public class MyColumn implements Column {

    private String name;
    private Type type;
    private List<Entry> entries;

    public MyColumn(String name, Type type, List<Entry> entries) {
        this.name = name;
        this.type = type;
        this.entries = entries;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public List<Entry> getData() {
        return entries;
    }

    @Override
    public MyColumn operate(String operator, Operable o) {
        List<Entry> newEntries = new ArrayList<>();
        if (o instanceof Column) {
            Column c = (Column) o;
            if (entries.size() != ((Column) o).getData().size()) {
                throw new UnsupportedOperationException("Cannot perform operation on columns with different length.");
            } // type mismatch is handled on Entry level
            if (entries.size() == 0) {
                throw new UnsupportedOperationException("Cannot perform operation on empty columns.");
            }
            for (int i = 0; i < entries.size(); i++) {
                newEntries.add((Entry) entries.get(i).operate(operator, c.get(i)));
            }
            return new MyColumn(name, newEntries.get(0).getType(), newEntries); // type needs to match new entries
        } else if (o instanceof Entry) {
            Entry e = (Entry) o;
            for (Entry entry : entries) {
                newEntries.add((Entry) entry.operate(operator, e));
            }
            return new MyColumn(name, newEntries.get(0).getType(), newEntries); // type needs to match new entries
        } else {
            throw new UnsupportedOperationException("Unsupported operation to '" + operator + "' this column " +
                    getTitle() + " with " + o.toString() + ".");
        }
    }

    @Override
    public String toString() {
        String s = getTitle();
        for (Entry e : entries) {
            s += "\n" + e.toString();
        }
        return s;
    }

    @Override
    public MyColumn add(Operable o) throws UnsupportedOperationException {
        return operate("+", o);
    }

    @Override
    public MyColumn subtract(Operable o) throws UnsupportedOperationException {
        return operate("-", o);
    }

    @Override
    public MyColumn multiply(Operable o) throws UnsupportedOperationException {
        return operate("*", o);
    }

    @Override
    public MyColumn divide(Operable o) throws UnsupportedOperationException {
        return operate("/", o);
    }
}
