package db.test;

import db.*;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyRowTest {
    List<Type> types = Arrays.asList(Type.INT, Type.STRING, Type.FLOAT);
    MyRow r1 = new MyRow(types);
    MyRow r2 = new MyRow(types).parse("-123,'Hello World',45.6789");
    MyRow r3 = new MyRow(types).parse("-123,'Hello World',4");

    @Test
    public void printTest() {
        assertEquals("0,'',0.000", r1.toString());
        assertEquals("-123,'Hello World',45.679", r2.toString());
        assertEquals("-123,'Hello World',4.000", r3.toString());
    }

    @Test(expected = RuntimeException.class)
    public void intParseTest() {
        MyRow r = new MyRow(types).parse("-123.456,'Hello World',45.6789");
    }

    @Test(expected = RuntimeException.class)
    public void floatParseTest() {
        MyRow r = new MyRow(types).parse("-123,'Hello World','wrong type'");
    }
}
