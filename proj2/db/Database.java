package db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database {

    public static final String DELIMITER = ",";

    private Map<String, Table> tables;
    private Parser parser;

    public Database() {
        tables = new HashMap<>();
        parser = new Parser(this);
    }

    public String transact(String query) {
        return parser.eval(query);
    }

    public Table joinTables(String[] names) {
        if (names == null) {
            throw new RuntimeException("Cannot join zero number of tables.");
        } else if (names.length == 1) {
            return getTableByName(names[0]);
        } else {
            Table t = getTableByName(names[0]);
            for (int i = 1; i < names.length; i++) { // note: starts from i = 1
                t = t.joinWith(getTableByName(names[i]));
            }
            return t;
        }
    }

    public void loadTableFromFile(File file) throws IOException {
        addTable(MyTable.createFromFile(file));
//        if (file == null) {
//            System.out.println("ERROR: File pointer is null.");
//            return false;
//        }
//        boolean status = false;
//        try {
//            addTable(MyTable.createFromFile(file));
//            status = true;
//        } catch (FileNotFoundException fe) {
//            System.out.println("ERROR: File " + file.getAbsolutePath() + " not found.");
//        } catch (IOException ioe) {
//            System.out.println("ERROR: Failed to read file " + file.getAbsolutePath() + ".");
//        } catch (RuntimeException re) {
//            System.out.println("ERROR: " + re.getMessage());
//        }
//        return status;
    }

    public void addTable(Table table) {
        tables.put(table.getName(), table);
    }

    public void dropTableByName(String name) {
        if (tables.containsKey(name)) {
            tables.remove(name);
        } else {
            throw new IllegalArgumentException("No such table: " + name);
        }
    }

    public Table getTableByName(String name) {
        if (tables.containsKey(name)) {
            return tables.get(name);
        } else {
            throw new IllegalArgumentException("No such table: " + name);
        }
    }

    public Set getTableNames() {
        return tables.keySet();
    }
}
