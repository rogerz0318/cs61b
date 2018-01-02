package db;

public class StdCondition implements Condition {

    private int index1;
    private int index2; // column index on the right side
    private String comparator;
    private Entry entry = null; // data entry on the right side

    public StdCondition(String comparable1, String comparator, String comparable2, Table table) {
        this.comparator = comparator;
        index1 = table.getAllColumnNames().indexOf(comparable1);
        index2 = table.getAllColumnNames().indexOf(comparable2);
        if (index1 == -1) { // no such index
            throw new IllegalArgumentException(comparable1 + " cannot be found in table '" + table.getName() + "'.");
        }
        if (index2 == -1) { // no such index for 2nd comparable, then it could be a literal (data entry)
            entry = EntryFactory.parse(comparable2);
        }
    }

    @Override
    public boolean isSatisfied(Row row) {
        Integer compared = null; // use integer to help identify failed comparison
        if (index2 != -1) {
            // compare two entries at two column indices
            compared = row.getData(index1).compareTo(row.getData(index2));
        } else {
            // compare data at column index to a constant entry
            compared = row.getData(index1).compareTo(entry);
        }
        switch (comparator) {
            // Valid conditional comparators are ==, !=, <, >, <= and >=.
            case "==":
                return compared == 0;
            case "!=":
                return compared != 0;
            case "<":
                return compared < 0;
            case ">":
                return compared > 0;
            case "<=":
                return compared <= 0;
            case ">=":
                return compared >= 0;
            default:
                throw new UnsupportedOperationException("Comparator '" + comparator + "' not supported.");
        }
    }
}
