package db;

import java.util.List;

public interface Column extends Operable {
    /**
     * Get the i-th row data. i starts from zero.
     * @param i
     * @return
     */
    Entry get(int i);

    /**
     * Get the column name.
     * @return
     */
    String getName();

    /**
     * Get the column type.
     * @return
     */
    Type getType();

    /**
     * Get the title of column which consists of
     * both its name and type, split by a space
     * @return
     */
    default String getTitle() {
        return getName() + " " + getType().name().toLowerCase();
    }

    /**
     * Get a list of data in this column.
     * @return
     */
    List<Entry> getData();
}
