package db.test;

import db.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntryTest {
    StringEntry s0 = new StringEntry();
    StringEntry s1 = new StringEntry("apple");
    StringEntry s2 = new StringEntry("pear");
    StringEntry s3 = new StringEntry("grape");
    StringEntry s4 = new StringEntry("grape");

    NumberEntry i0 = new NumberEntry(0);
    NumberEntry i1 = new NumberEntry(2);
    NumberEntry i2 = new NumberEntry(3);
    NumberEntry i3 = new NumberEntry(5);
    NumberEntry i4 = new NumberEntry(5);
    NumberEntry i_inf = i1.divide(i0);

    NumberEntry f0 = new NumberEntry(0.0);
    NumberEntry f1 = new NumberEntry(-1.5);
    NumberEntry f2 = new NumberEntry(3.012345);
    NumberEntry f3 = new NumberEntry(5.5);
    NumberEntry f4 = new NumberEntry(5.5);
    NumberEntry f5 = new NumberEntry(5.0);
    NumberEntry f6 = new NumberEntry(1375.0188236);
    NumberEntry f_inf = f1.divide(f0);

    @Test
    public void compareTest() {
        assertTrue(s0.compareTo(s1) < 0);
        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s2.compareTo(s3) > 0);
        assertTrue(s3.compareTo(s4) == 0);

        assertTrue(i0.compareTo(i1) < 0);
        assertTrue(i1.compareTo(i2) < 0);
        assertTrue(i3.compareTo(i2) > 0);
        assertTrue(i3.compareTo(i4) == 0);
        assertTrue(i_inf.compareTo(i4) > 0);
        assertTrue(i_inf.compareTo(f_inf) == 0);

        assertTrue(f1.compareTo(f0) < 0);
        assertTrue(f3.compareTo(f2) > 0);
        assertTrue(f3.compareTo(f4) == 0);
        assertTrue(f3.compareTo(f_inf) < 0);

        assertTrue(i1.compareTo(f4) < 0);
        assertTrue(f3.compareTo(i3) > 0);
        assertTrue(f5.compareTo(i4) == 0);
        assertTrue(f_inf.compareTo(i_inf) == 0);
    }

    @Test
    public void printTest() {
        assertEquals("NOVALUE", s0.toString());
        assertEquals("'apple'", s1.toString());

        assertEquals("0", i0.toString());
        assertEquals("5", i3.toString());

        assertEquals("0.000", f0.toString());
        assertEquals("-1.500", f1.toString());
        assertEquals("1375.019", f6.toString());

        assertEquals("NaN", f_inf.toString());
        assertEquals("NaN", i_inf.toString());
    }

    @Test
    public void arithmOpsTest() {
        assertEquals("'apple'", s0.add(s1).toString());
        assertEquals("'applepear'", s1.add(s2).toString());
        assertEquals("7", i1.add(i3).toString());
        assertEquals("1.512", f1.add(f2).toString());
        assertEquals("8.500", i2.add(f4).toString());
        assertEquals("NaN", i_inf.add(f6).toString());

        assertEquals("-2", i2.subtract(i4).toString());
        assertEquals("0.500", f4.subtract(f5).toString());
        assertEquals("-2", i2.subtract(i4).toString());
        assertEquals("-6.500", f1.subtract(i4).toString());
        assertEquals("NaN", f1.subtract(i_inf).toString());

        assertEquals("11.000", f3.multiply(i1).toString());
        assertEquals("0", i0.multiply(i1).toString());
        assertEquals("NaN", f_inf.multiply(i1).toString());

        assertEquals("1.000", f4.divide(f3).toString());
        assertEquals("0.400", i1.divide(f5).toString());
        assertEquals("NaN", i_inf.divide(i0).toString());
        assertEquals("NaN", f3.divide(f_inf).toString());
    }

    @Test(expected = RuntimeException.class)
    public void compareExceptionTest() {
        s1.compareTo(i2);
    }

    @Test(expected = RuntimeException.class)
    public void opsExceptionTest() {
        i2.add(s3);
    }
}
