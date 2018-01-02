package db;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Table {
    /**
     * Returns a new table that is the natural inner join of this table and the given table.
     * If there is no column in common, returns the Cartesian-product version.
     * @param name
     * @param table
     * @return
     */
    Table joinWith(Table table);

    /**
     * Write the current table to file.
     * @param file
     * @throws IOException
     */
    void writeToFile(File file) throws IOException;

    /**
     * Insert a row at the end of this table.
     * @param row
     */
    void insertRow(Row row);

    /**
     * Returns a new table filtered by the given condition.
     * @param c
     * @return
     */
    Table filterBy(Condition c);

    /**
     * Returns a new table whose columns are selected by the given column expressions.
     * @param ces
     * @return
     */
    Table selectBy(List<ColumnExpr> ces);

    /**
     * Returns the name of this table.
     * @return
     */
    String getName();

    /**
     * Rename the table.
     * @param name
     * @return
     */
    void setName(String name);

    /**
     * Returns a list of all column names in order.
     * @return
     */
    List<String> getAllColumnNames();

    /**
     * Returns a list of all column types in order.
     * @return
     */
    List<Type> getAllColumnTypes();

    /**
     * Returns a list of all rows in order.
     * @return
     */
    List<Row> getAllRows();

    /**
     * Returns a column with given name from this table. If there is no such column, return null.
     * @param name
     * @return
     */
    Column getColumnByName(String name);

    @Override
    String toString();
}
