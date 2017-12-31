package db;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Table {

    Table joinWith(String name, Table table);

    void writeToFile(File file) throws IOException;

    void insertRow(Row row);

    Table filterBy(Condition c);

    Table selectBy(ColumnExpression ce);

    String getName();

    List<String> getAllColumnNames();

    List<Type> getAllColumnTypes();

    List<Row> getAllRows();

    @Override
    String toString();
}
