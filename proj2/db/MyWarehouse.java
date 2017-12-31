package db;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyWarehouse implements Warehouse {

    private Map<String, Table> tables;

    public MyWarehouse() {
        tables = new HashMap<>();
    }

    @Override
    public boolean load(File file) {
        if (file == null) {
            System.out.println("ERROR: File pointer is null.");
            return false;
        }
        boolean status = false;
        try {
            add(MyTable.createFromFile(file));
            status = true;
        } catch (FileNotFoundException fe) {
            System.out.println("ERROR: File " + file.getAbsolutePath() + " not found.");
        } catch (IOException ioe) {
            System.out.println("ERROR: Failed to read file " + file.getAbsolutePath() + ".");
        } catch (RuntimeException re) {
            System.out.println("ERROR: " + re.getMessage());
        }
        return status;
    }

    @Override
    public void add(Table table) {
        tables.put(table.getName(), table);
    }

    @Override
    public Table get(String name) {
        return tables.get(name);
    }

    @Override
    public Set getTableNames() {
        return tables.keySet();
    }
}
