package db.test;

import db.*;
import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;
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
        MyTable teams = MyTable.createFromFile(new File("db/test/teams.tbl"));
        ColumnExpr ce1 = new BinaryColumnExpr("TeamName", "+", "'_from_Mars'", "MarsTeamName");
        ColumnExpr ce2 = new BinaryColumnExpr("YearEstablished", "+", "1000", "AThousandYearsLater");
        ColumnExpr ce3 = new UnaryColumnExpr("Stadium");
        String expected = "MarsTeamName string,AThousandYearsLater int,Stadium string\n" +
                "'Mets_from_Mars',2962,'Citi Field'\n" +
                "'Steelers_from_Mars',2933,'Heinz Field'\n" +
                "'Patriots_from_Mars',2960,'Gillette Stadium'\n" +
                "'Cloud9_from_Mars',3012,NOVALUE\n" +
                "'EnVyUs_from_Mars',3007,NOVALUE\n" +
                "'Golden Bears_from_Mars',2886,'Memorial Stadium'";
        assertEquals(expected, teams.selectBy(Arrays.asList(ce1, ce2, ce3)).toString());
    }

    @Test
    public void filterByTest() throws Exception {
        MyTable teamsrecords = MyTable.createFromFile(new File("db/test/teams+records.tbl"));
        StdCondition cond1 = new StdCondition("Sport", "==", "'NFL Football'", teamsrecords);
        StdCondition cond2 = new StdCondition("Season", ">=", "2014", teamsrecords);
        String expected = "TeamName string,City string,Sport string,YearEstablished int,Mascot string," +
                "Stadium string,Season int,Wins int,Losses int,Ties int\n" +
                "'Steelers','Pittsburgh','NFL Football',1933,'Steely McBeam','Heinz Field',2015,10,6,0\n" +
                "'Steelers','Pittsburgh','NFL Football',1933,'Steely McBeam','Heinz Field',2014,11,5,0\n" +
                "'Patriots','New England','NFL Football',1960,'Pat Patriot','Gillette Stadium',2015,12,4,0\n" +
                "'Patriots','New England','NFL Football',1960,'Pat Patriot','Gillette Stadium',2014,12,4,0";
        String actual = teamsrecords.filterBy(cond1).filterBy(cond2).toString();
        assertEquals(expected, actual);
    }
}
