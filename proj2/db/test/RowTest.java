package db.test;

import db.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class RowTest {
    List<Type> types = Arrays.asList(Type.INT, Type.STRING, Type.FLOAT);
    MyRow r1 = new MyRow(types);
    MyRow r2 = new MyRow(new String[]{"-123", "Hello World", "45.6789"}, types);
    MyRow r3 = new MyRow(new String[]{"-123", "Hello World", "4"}, types);

    @Test
    public void printTest() {
        assertEquals("0,NOVALUE,0.000", r1.toString());
        assertEquals("-123,'Hello World',45.679", r2.toString());
        assertEquals("-123,'Hello World',4.000", r3.toString());
    }

    @Test(expected = RuntimeException.class)
    public void intParseTest() {
        MyRow r = new MyRow(new String[]{"-123.234", "Hello World", "45.6789"}, types);
    }

    @Test(expected = RuntimeException.class)
    public void floatParseTest() {
        MyRow r = new MyRow(new String[]{"-123", "Hello World", "wrong type"}, types);
    }
}
