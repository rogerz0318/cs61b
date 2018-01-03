package db;

import java.io.*;
import java.util.*;

public class MyTable implements Table {

    private static String delimiter = ",";
    private static final String DEFAULT_NAME = "untitled";
    private String name;
    private List<Row> rows;
    private List<Type> columnTypes;
    private List<String> columnNames;

    /**
     * Takes a column title, parse into column names and types. The column title should take this form: "UserID
     * string,Lastname string,Age int", with column name and type separated by a space, and titles separated by
     * delimiters.
     *
     * @param columnTitle
     */
    public MyTable(String name, String columnTitle) {
        this(name, columnTitle.split("\\s*,\\s*"));
    }

    public MyTable(String name, String[] columnTitles) {
        this.name = name;
        columnTypes = new ArrayList<>();
        columnNames = new ArrayList<>();
        for (String title : columnTitles) {
            int p = title.lastIndexOf(" "); // look for last occurrence of space
            columnNames.add(title.substring(0, p).trim()); // first part is name (spaces trimmed)
            columnTypes.add(Type.valueOf(title.substring(p + 1).toUpperCase())); // last part is type
        }
        rows = new ArrayList<>();
    }

    public static MyTable createFromFile(File file) throws IOException {
        MyTable t = null;
        String line = null;
        // Get the file name without extension as the table name.
        String fileName = file.getName();
        int dot = fileName.lastIndexOf(".");
        String tableName = fileName.substring(0, dot);
        // Read file and parse line
        BufferedReader br = new BufferedReader(new FileReader(file));
        line = br.readLine();
        t = new MyTable(tableName, line);
        while ((line = br.readLine()) != null) {
            t.insertRow(new MyRow(t.columnTypes).parse(line));
        }
        br.close();
        return t;
    }

    private MyTable() {
        name = DEFAULT_NAME;
        rows = new ArrayList<>();
        columnTypes = new ArrayList<>();
        columnNames = new ArrayList<>();
    }

    public static void setDelimiter(String s) {
        delimiter = s;
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
    public List<String> getAllColumnNames() {
        return columnNames;
    }

    @Override
    public List<Type> getAllColumnTypes() {
        return columnTypes;
    }

    @Override
    public List<Row> getAllRows() {
        return rows;
    }

    @Override
    public MyColumn getColumnByName(String name) {
        if (!columnNames.contains(name)) {
            return null;
        }
        int index = columnNames.indexOf(name);
        List<Entry> entries = new ArrayList<>();
        for (Row row : rows) {
            entries.add(row.getData(index));
        }
        return new MyColumn(name, columnTypes.get(index), entries);
    }

    private MyTable doCartesianProduct(Table table) {
        List<String> joinedNames = new ArrayList<>();
        List<Type> joinedTypes = new ArrayList<>();
        List<Row> joinedRows = new ArrayList<>();

        // String and Type are immutable, so safe to directly addTable to lists
        joinedNames.addAll(columnNames);
        joinedNames.addAll(table.getAllColumnNames());
        joinedTypes.addAll(columnTypes);
        joinedTypes.addAll(table.getAllColumnTypes());

        // Rows are mutable (at least for MyRow), so need to make copies
        for (Row r1 : rows) {
            for (Row r2 : table.getAllRows()) {
                List<Entry> merged = new ArrayList<>();
                merged.addAll(r1.copy().getAllData());
                merged.addAll(r2.copy().getAllData());
                joinedRows.add(new MyRow(merged.toArray(new Entry[0])));
            }
        }

        // Make new table
        MyTable t = new MyTable();
        t.name = DEFAULT_NAME;
        t.columnNames = joinedNames;
        t.columnTypes = joinedTypes;
        t.rows = joinedRows;

        return t;
    }

    /**
     * Performs natural inner join with another table. In a natural inner join, the new table's rows are formed by
     * merging pairs of rows from the input tables. Two rows should be merged if and only if all of their shared columns
     * have the same values. In the case that the input tables have no columns in common, the resulting table is what is
     * called the Cartesian Product of the tables. That is, each row of table A is considered to match each row of table
     * B as if they had a column in common.
     *
     * @param table
     * @return
     */
    @Override
    public MyTable joinWith(Table table) {
        // find the shared columns
        List<String> sharedColumnNames = new ArrayList<>();
        List<Type> sharedColumnTypes = new ArrayList<>();
        Map<Integer, Integer> sharedIndex = new LinkedHashMap<>();

        for (int i = 0; i < columnNames.size(); i++) {
            String name1 = columnNames.get(i);
            for (int j = 0; j < table.getAllColumnNames().size(); j++) {
                String name2 = table.getAllColumnNames().get(j);
                if (name1.equals(name2)) { // potentially need to check if type matches as well
                    sharedColumnNames.add(name1);
                    sharedColumnTypes.add(columnTypes.get(i)); // just store this table's column type if match
                    // i, j corresponds to two data entries that belongs to the same shared column
                    sharedIndex.put(i, j);
                }
            }
        }

        if (sharedIndex.isEmpty()) {
            return doCartesianProduct(table);
        } else {
            // otherwise do inner join
            List<Row> joinedRows = new ArrayList<>();
            for (Row r1 : rows) {
                for (Row r2 : table.getAllRows()) {
                    boolean match = true;
                    for (int i : sharedIndex.keySet()) {
                        int j = sharedIndex.get(i);
                        boolean dataMatches = r1.getData(i).compareTo(r2.getData(j)) == 0;
                        match = match && dataMatches;
                    }
                    if (match) {
                        // merge the matched row
                        List<Entry> merged = new ArrayList<>(); // store entries after merge
                        List<Entry> copyOfRow1 = r1.copy().getAllData();
                        List<Entry> copyOfRow2 = r2.copy().getAllData();
                        for (int k : sharedIndex.keySet()) {
                            merged.add(copyOfRow1.get(k)); // addTable the overlapped data in order of the first row
                        }
                        for (int i = 0; i < copyOfRow1.size(); i++) {
                            if (!sharedIndex.keySet().contains(i)) {
                                merged.add(copyOfRow1.get(i)); // addTable from row 1, excluding the matched entries
                            }
                        }
                        for (int j = 0; j < copyOfRow2.size(); j++) {
                            if (!sharedIndex.values().contains(j)) {
                                merged.add(copyOfRow2.get(j)); // addTable from row 2, excluding the matched entries
                            }
                        }
                        joinedRows.add(new MyRow(merged.toArray(new Entry[0])));
                    }
                }
            }

            // make all joined table instance variables
            List<String> names1 = columnNames;
            List<String> names2 = table.getAllColumnNames();
            List<Type> types1 = columnTypes;
            List<Type> types2 = table.getAllColumnTypes();

            List<String> joinedNames = new ArrayList<>();
            List<Type> joinedTypes = new ArrayList<>();

            // addTable the overlapped names in order of the first row
            joinedNames.addAll(sharedColumnNames);
            joinedTypes.addAll(sharedColumnTypes);
            for (int i = 0; i < names1.size(); i++) {
                if (!sharedIndex.keySet().contains(i)) {
                    // addTable from this table, excluding the matched columns
                    joinedNames.add(names1.get(i));
                    joinedTypes.add(types1.get(i));
                }
            }
            for (int j = 0; j < names2.size(); j++) {
                if (!sharedIndex.values().contains(j)) {
                    // addTable from row 2, excluding the matched entries
                    joinedNames.add(names2.get(j));
                    joinedTypes.add(types2.get(j));
                }
            }

            // make table with merged instance variables
            MyTable t = new MyTable();
            t.name = DEFAULT_NAME;
            t.columnNames = joinedNames;
            t.columnTypes = joinedTypes;
            t.rows = joinedRows;

            return t;
        }
    }

    @Override
    public void writeToFile(File file) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(titleRow());
        for (Row r : rows) {
            bw.newLine();
            bw.write(r.toString());
        }
        bw.close();
    }

    private boolean isTypeCompatible(Row row) {
        if (row.getAllData().size() != columnTypes.size()) {
            return false;
        }
        boolean compatible = true;
        for (int i = 0; i < columnTypes.size(); i++) {
            compatible = compatible && row.getData(i).getType() == columnTypes.get(i);
        }
        return compatible;
    }

    @Override
    public void insertRow(Row row) {
        if (isTypeCompatible(row)) {
            rows.add(row);
        } else {
            throw new IllegalArgumentException("Cannot insertRow row " + row + " into table, type is incompatible.");
        }
    }

    @Override
    public MyTable filterBy(Condition c) {
        MyTable t = new MyTable();
        t.columnNames.addAll(columnNames);
        t.columnTypes.addAll(columnTypes);
        for (Row row : rows) {
            if (c.isSatisfied(row)) {
                t.rows.add(row.copy());
            }
        }
        return t;
    }

    @Override
    public MyTable selectBy(List<ColumnExpr> ces) {
        if (ces == null || ces.size() == 0) {
            throw new IllegalArgumentException("Cannot select new table from null or " +
                    "empty list of column expressions.");
        }
        MyTable t = new MyTable();
        // collect all columns
        List<Column> columns = new ArrayList<>();
        for (ColumnExpr ce : ces) {
            Column c = ce.evaluate(this);
            columns.add(c);
            t.columnNames.add(c.getName());
            t.columnTypes.add(c.getType());
        }
        int numRows = columns.get(0).getData().size();
        // build new table from collected columns
        for (int i = 0; i < numRows; i++) {
            List<Entry> entries = new ArrayList<>(); // for temporary storage of entries in this i-th row
            for (Column c : columns) {
                entries.add(c.get(i));
            }
            t.rows.add(new MyRow(entries.toArray(new Entry[0])));
        }
        return t;
    }

    private String titleRow() {
        String s = "";
        for (int i = 0; i < columnTypes.size(); i++) {
            s += delimiter + columnNames.get(i) + " " + columnTypes.get(i).name().toLowerCase();
        }
        return s.substring(delimiter.length());
    }

    @Override
    public String toString() {
        String s = titleRow();
        for (Row r : rows) {
            s += "\n" + r.toString();
        }
        return s;
    }
}
