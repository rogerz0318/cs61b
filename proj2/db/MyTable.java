package db;

import java.io.File;

public class MyTable implements Table {
    @Override
    public Table join(Table table) {
        return null;
    }

    @Override
    public Table load(File file) {
        return null;
    }

    @Override
    public boolean store(File file) {
        return false;
    }

    @Override
    public boolean insert(String[] values) {
        return false;
    }

    @Override
    public Table filter(Condition c) {
        return null;
    }

    @Override
    public Table select(ColumnExpression ce) {
        return null;
    }
}
