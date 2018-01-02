package db.test;

import db.*;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MyTableTest {
    @Test
    public void joinTableTest() throws Exception {
        MyTable t1 = MyTable.createFromFile(new File("db/test/t1.tbl"));
        String t1_original = t1.toString();
        MyTable t2 = MyTable.createFromFile(new File("db/test/t2.tbl"));
        String t2_original = t2.toString();
        MyTable t3 = t1.joinWith(t2);
        MyTable t3_expected = MyTable.createFromFile(new File("db/test/t3.tbl"));
        assertEquals(t3.toString(), t3_expected.toString());
        // make sure original table is not modified
        assertEquals(t1_original, t1.toString());
        assertEquals(t2_original, t2.toString());

        MyTable t4 = MyTable.createFromFile(new File("db/test/t4.tbl"));
        String t4_original = t4.toString();
        MyTable t5 = t3.joinWith(t4);
        MyTable t5_expected = MyTable.createFromFile(new File("db/test/t5.tbl"));
        assertEquals(t5.toString(), t5_expected.toString());
        // make sure original table is not modified
        assertEquals(t4_original, t4.toString());

        MyTable teams = MyTable.createFromFile(new File("db/test/teams.tbl"));
        MyTable records = MyTable.createFromFile(new File("db/test/records.tbl"));
        MyTable joined = teams.joinWith(records);
        MyTable joined_expected = MyTable.createFromFile(new File("db/test/teams+records.tbl"));
        assertEquals(joined_expected.toString(), joined.toString());
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

    @Test
    public void selectByTest() throws Exception {
        MyTable records = MyTable.createFromFile(new File("db/test/teams.tbl"));
        ColumnExpr ce1 = new MyColumnExpr("TeamName", "+", "'_from_Mars'", "MarsTeamName");
        ColumnExpr ce2 = new MyColumnExpr("YearEstablished", "+", "1000");
        String expected = "MarsTeamName string,YearEstablished int\n" +
                "'Mets_from_Mars',2962\n" +
                "'Steelers_from_Mars',2933\n" +
                "'Patriots_from_Mars',2960\n" +
                "'Cloud9_from_Mars',3012\n" +
                "'EnVyUs_from_Mars',3007\n" +
                "'Golden Bears_from_Mars',2886";
        assertEquals(expected, records.selectBy(Arrays.asList(ce1, ce2)).toString());
    }
}
