import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addFirst() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addFirst("World");
        assertEquals(deque.toString(), "World");
        deque.addFirst("Hello");
        assertEquals(deque.toString(), "Hello World");
    }

    @Test
    public void addLast() {
        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.addLast("World");
        assertEquals(deque.toString(), "World");
        deque.addLast("Hello");
        assertEquals(deque.toString(), "World Hello");
    }

    @Test
    public void isEmpty() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertTrue(deque.isEmpty());
        deque.addFirst(47);
        assertFalse(deque.isEmpty());
    }

    @Test
    public void size() {
        ArrayDeque<Double> deque = new ArrayDeque<>();
        assertEquals(0, deque.size());
        deque.addFirst(1.0);
        assertEquals(1, deque.size());
        deque.addLast(2.0);
        assertEquals(2, deque.size());
        deque.removeFirst();
        assertEquals(1, deque.size());
        deque.removeLast();
        assertEquals(0, deque.size());
    }

    @Test
    public void toStringTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(3, 5, 8, 13, 21);
        assertEquals("3 5 8 13 21", deque.toString());
        deque = new ArrayDeque<>();
        assertEquals("", deque.toString());
    }

    @Test
    public void removeFirst() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(3, 5, 8, 13, 21);
        assertEquals((Integer)3, deque.removeFirst());
        assertEquals("5 8 13 21", deque.toString());
        deque = new ArrayDeque<>();
        assertNull(deque.removeFirst());
    }

    @Test
    public void removeLast() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(3, 5, 8, 13, 21);
        assertEquals((Integer)21, deque.removeLast());
        assertEquals("3 5 8 13", deque.toString());
        deque = new ArrayDeque<>();
        assertNull(deque.removeLast());
    }

    @Test
    public void get() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(3, 5, 8, 13, 21);
        assertEquals((Integer)3, deque.get(0));
        assertEquals((Integer)21, deque.get(4));
        assertEquals((Integer)13, deque.get(3));
    }

    @Test
    public void resizeTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>(3, 5, 8, 13, 21);
        for (int i = 0; i < 5; i++) deque.addFirst(1);
        for (int i = 0; i < 50; i++) deque.addLast(2);
        assertEquals((Integer)1, deque.get(0));
        assertEquals((Integer)5, deque.get(6));
        assertEquals((Integer)2, deque.get(12));
        for (int i = 0; i < 9; i++) deque.removeFirst();
        for (int i = 0; i < 45; i++) deque.removeLast();
        assertEquals("21 2 2 2 2 2", deque.toString());
    }

//    @Test
//    public void findIndex() {
//        ArrayDeque<Integer> deque = new ArrayDeque<>(5);
//        assertEquals(3, deque.findIndex(1, 2));
//        assertEquals(0, deque.findIndex(0, 0));
//        assertEquals(2, deque.findIndex(2, 5));
//        assertEquals(1, deque.findIndex(0, -4));
//    }
}