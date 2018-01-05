package db.test;

import db.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MyColumnTest {

    private MyColumn c_int;
    private MyColumn c_float;
    private MyColumn c_string;

    @Before
    public void setUp() {
        List<Entry> ints = new ArrayList<>();
        List<Entry> floats = new ArrayList<>();
        List<Entry> strings = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ints.add(new NumberEntry(i));
            floats.add(new NumberEntry((double)i + 0.1));
            strings.add(new StringEntry(String.valueOf(i)));
        }
        c_int = new MyColumn("Integers", Type.INT, ints);
        c_float = new MyColumn("Floats", Type.FLOAT, floats);
        c_string = new MyColumn("Strings", Type.STRING, strings);
    }

    @Test
    public void printTest() {
        assertEquals("Integers int\n0\n1\n2\n3\n4", c_int.toString());
        assertEquals("Floats float\n0.100\n1.100\n2.100\n3.100\n4.100", c_float.toString());
        assertEquals("Strings string\n'0'\n'1'\n'2'\n'3'\n'4'", c_string.toString());
    }

    @Test
    public void operationsTest() {
        // column on column
        assertEquals("Integers int\n0\n2\n4\n6\n8", c_int.add(c_int).toString());
        assertEquals("Floats float\n0.100\n2.100\n4.100\n6.100\n8.100", c_float.add(c_int).toString());
        assertEquals("Floats float\nNaN\n1.100\n1.050\n1.033\n1.025", c_float.divide(c_int).toString());
        assertEquals("Strings string\n'00'\n'11'\n'22'\n'33'\n'44'", c_string.add(c_string).toString());

        // column on entry
        assertEquals("Strings string\n'0s'\n'1s'\n'2s'\n'3s'\n'4s'",
                c_string.add(new StringEntry("s")).toString());
        assertEquals("Integers float\n0.000\n0.500\n1.000\n1.500\n2.000",
                c_int.multiply(new NumberEntry(0.5)).toString());
        assertEquals("Integers int\n0\n0\n1\n1\n2",
                c_int.divide(new NumberEntry(2)).toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void operationsExceptionTest() {
        c_float.add(c_string);
    }
}
