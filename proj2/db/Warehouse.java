package db;

import java.io.File;
import java.util.Set;

public interface Warehouse {

    /**
     * Load table from file, returns true if load is successful.
     * @param file
     * @return
     */
    boolean load(File file);

    /**
     * Add a table to the warehouse and assign name to it.
     * @param table
     */
    void add(Table table);

    /**
     * Returns a table associated with given name.
     * @param name
     * @return
     */
    Table get(String name);

    /**
     * Returns a set of all table names in strings.
     * @return
     */
    Set getTableNames();
}
