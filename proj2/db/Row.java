package db;

public interface Row {

    /**
     * Get a data entry by column name.
     * @param columnName
     * @return
     */
    Entry get(String columnName);

    @Override
    String toString();
}
