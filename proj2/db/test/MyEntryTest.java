package db.test;

import db.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyEntryTest {
    StringEntry s = new StringEntry();
    StringEntry s_apple = new StringEntry("apple");
    StringEntry s_pear = new StringEntry("pear");
    StringEntry s_grape = new StringEntry("grape");
    StringEntry sa_grape = new StringEntry("grape");
    StringEntry s_noval = new StringEntry().parse("NOVALUE");

    NumberEntry i_0 = new NumberEntry(0);
    NumberEntry i_2 = new NumberEntry(2);
    NumberEntry i_3 = new NumberEntry(3);
    NumberEntry i_5 = new NumberEntry(5);
    NumberEntry i_5a = new NumberEntry(5);
    NumberEntry i_nan = i_2.divide(i_0);
    NumberEntry i_noval = new NumberEntry(2).parse("NOVALUE");

    NumberEntry f_0_0 = new NumberEntry(0.0);
    NumberEntry f_1_5 = new NumberEntry(-1.5);
    NumberEntry f_3_012345 = new NumberEntry(3.012345);
    NumberEntry f_5_5 = new NumberEntry(5.5);
    NumberEntry fa_5_5 = new NumberEntry(5.5);
    NumberEntry f_5_0 = new NumberEntry(5.0);
    NumberEntry f_1375_0188236 = new NumberEntry(1375.0188236);
    NumberEntry f_nan = f_1_5.divide(f_0_0);
    NumberEntry f_noval = new NumberEntry(1.0).parse("NOVALUE");

    @Test
    public void compareTest() {
        assertTrue(s.compareTo(s_apple) < 0);
        assertTrue(s_apple.compareTo(s_pear) < 0);
        assertTrue(s_pear.compareTo(s_grape) > 0);
        assertTrue(s_grape.compareTo(sa_grape) == 0);

        assertTrue(i_0.compareTo(i_2) < 0);
        assertTrue(i_2.compareTo(i_3) < 0);
        assertTrue(i_5.compareTo(i_3) > 0);
        assertTrue(i_5.compareTo(i_5a) == 0);
        assertTrue(i_nan.compareTo(i_5a) > 0);
        assertTrue(i_nan.compareTo(f_nan) == 0);

        assertTrue(f_1_5.compareTo(f_0_0) < 0);
        assertTrue(f_5_5.compareTo(f_3_012345) > 0);
        assertTrue(f_5_5.compareTo(fa_5_5) == 0);
        assertTrue(f_5_5.compareTo(f_nan) < 0);

        assertTrue(i_2.compareTo(fa_5_5) < 0);
        assertTrue(f_5_5.compareTo(i_5) > 0);
        assertTrue(f_5_0.compareTo(i_5a) == 0);
        assertTrue(f_nan.compareTo(i_nan) == 0);
    }

    @Test
    public void printTest() {
        assertEquals("''", s.toString());
        assertEquals("'apple'", s_apple.toString());

        assertEquals("0", i_0.toString());
        assertEquals("5", i_5.toString());

        assertEquals("0.000", f_0_0.toString());
        assertEquals("-1.500", f_1_5.toString());
        assertEquals("1375.019", f_1375_0188236.toString());

        assertEquals("NaN", f_nan.toString());
        assertEquals("NaN", i_nan.toString());

        assertEquals("NOVALUE", i_noval.toString());
        assertEquals("NOVALUE", f_noval.toString());
        assertEquals("NOVALUE", s_noval.toString());
    }

    @Test
    public void arithmOpsTest() {
        assertEquals("'apple'", s.add(s_apple).toString());
        assertEquals("'applepear'", s_apple.add(s_pear).toString());
        assertEquals("7", i_2.add(i_5).toString());
        assertEquals("1.512", f_1_5.add(f_3_012345).toString());
        assertEquals("8.500", i_3.add(fa_5_5).toString());
        assertEquals("NaN", i_nan.add(f_1375_0188236).toString());

        assertEquals("-2", i_3.subtract(i_5a).toString());
        assertEquals("0.500", fa_5_5.subtract(f_5_0).toString());
        assertEquals("-2", i_3.subtract(i_5a).toString());
        assertEquals("-6.500", f_1_5.subtract(i_5a).toString());
        assertEquals("NaN", f_1_5.subtract(i_nan).toString());

        assertEquals("11.000", f_5_5.multiply(i_2).toString());
        assertEquals("0", i_0.multiply(i_2).toString());
        assertEquals("NaN", f_nan.multiply(i_2).toString());

        assertEquals("1.000", fa_5_5.divide(f_5_5).toString());
        assertEquals("0.400", i_2.divide(f_5_0).toString());
        assertEquals("NaN", i_nan.divide(i_0).toString());
        assertEquals("NaN", f_5_5.divide(f_nan).toString());

        assertEquals("5.500", f_5_5.add(f_noval).toString());
        assertEquals("-5.000", f_noval.subtract(i_5).toString());
        assertEquals("NOVALUE", f_noval.multiply(i_noval).toString());
        assertEquals("NaN", f_3_012345.divide(i_noval).toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void compareExceptionTest() {
        s_apple.compareTo(i_3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void opsExceptionTest() {
        i_3.add(s_grape);
    }
}
