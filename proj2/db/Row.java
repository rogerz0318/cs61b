package db;

public interface Row {

    /**
     * Get a data entry by index.
     * @param index
     * @return
     */
    Entry get(int index);

    @Override
    String toString();
}
