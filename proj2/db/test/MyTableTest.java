package db.test;

import db.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class MyTableTest {
    @Test
    public void joinTableTest() throws Exception {
        MyTable t1 = MyTable.createFromFile(new File("db/test/t1.tbl"));
        MyTable t2 = MyTable.createFromFile(new File("db/test/t2.tbl"));
        MyTable t3 = t1.joinWith("t3", t2);
        MyTable t3_expected = MyTable.createFromFile(new File("db/test/t3.tbl"));
        assertEquals(t3.toString(), t3_expected.toString());

        MyTable t4 = MyTable.createFromFile(new File("db/test/t4.tbl"));
        MyTable t5 = t3.joinWith("t5", t4);
        MyTable t5_expected = MyTable.createFromFile(new File("db/test/t5.tbl"));
        assertEquals(t5.toString(), t5_expected.toString());

        // TODO: more complicated tables for tests
    }

    @Test
    public void tableReadWriteTest() throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("db/test/test.tbl"));
        String line;
        String expected = br.readLine();
        while ((line = br.readLine()) != null) {
            expected += "\n" + line;
        }
        MyTable t = MyTable.createFromFile(new File("db/test/test.tbl"));
        assertEquals(expected, t.toString());
        t.writeToFile(new File("db/test/test_write.tbl"));
        assertEquals(expected, MyTable.createFromFile(new File("db/test/test_write.tbl")).toString());
    }

    @Test(expected = IOException.class)
    public void tableReadExceptionTest() throws Exception {
        MyTable.createFromFile(new File("db/test/non-exist.tbl"));
    }

    @Test
    public void insertRowTest() throws Exception {
        String insert = "'Fantasy Team',2099,50,10,3";
        MyTable t = MyTable.createFromFile(new File("db/test/test.tbl"));
        String expected = t.toString() + "\n" + insert;
        t.insertRow(new MyRow(t.getAllColumnTypes()).parse(insert));
        assertEquals(expected, t.toString());

        insert = "NOVALUE,NOVALUE,NOVALUE,NOVALUE";
        t = MyTable.createFromFile(new File("db/test/test.tbl"));
        expected = t.toString() + "\n" + insert + ",0";
        t.insertRow(new MyRow(t.getAllColumnTypes()).parse(insert));
        assertEquals(expected, t.toString());
    }

    @Test(expected = RuntimeException.class)
    public void insertRowExceptionTest() throws Exception {
        String insert = "'Fantasy Team',2099,50.12345,10,3";
        MyTable t = MyTable.createFromFile(new File("db/test/test.tbl"));
        t.insertRow(new MyRow(t.getAllColumnTypes()).parse(insert));
    }
}
