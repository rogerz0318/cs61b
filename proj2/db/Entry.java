package db;

public interface DataEntry extends Comparable<DataEntry>, Operable {

    @Override
    int compareTo(DataEntry d); // for comparison operations: ==, !=, <, >, <= and >=

    @Override
    String toString(); // return String representation

}
