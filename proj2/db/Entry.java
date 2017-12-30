package db;

public interface Entry extends Comparable<Entry>, Operable {

    @Override
    int compareTo(Entry entry); // for comparison operations: ==, !=, <, >, <= and >=

    @Override
    String toString(); // return String representation

    /**
     * Returns an entry whose data can be represented by the given string,
     * or this entry self if s is null or empty string.
     * @param s
     * @return
     */
    Entry parse(String s);
}
