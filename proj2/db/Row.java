package db;

import java.util.List;

public interface Row {

    /**
     * Returns a list of all data entries.
     * @return
     */
    List<Entry> getAllData();

    /**
     * Returns a data entry by index.
     * @param index
     * @return
     */
    default Entry getData(int index) {
        return getAllData().get(index);
    }

    /**
     * Parses the given string and put data in this row,
     * and returns itself.
     * @param s
     * @return
     */
    Row parse(String s);

    /**
     * Returns a deep copy of this row.
     * @return
     */
    Row copy();

    @Override
    String toString();
}
