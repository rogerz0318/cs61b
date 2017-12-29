package db;

public interface Entry extends Comparable<Entry>, Operable {

    @Override
    int compareTo(Entry entry); // for comparison operations: ==, !=, <, >, <= and >=

    @Override
    String toString(); // return String representation
}
