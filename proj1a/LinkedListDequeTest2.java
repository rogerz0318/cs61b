import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedListDequeTest2 {

    @Test
    public void addFirst() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();
        deque.addFirst("World");
        assertEquals(deque.toString(), "World");
        deque.addFirst("Hello");
        assertEquals(deque.toString(), "Hello World");
    }

    @Test
    public void addLast() {
        LinkedListDeque<String> deque = new LinkedListDeque<>();
        deque.addLast("World");
        assertEquals(deque.toString(), "World");
        deque.addLast("Hello");
        assertEquals(deque.toString(), "World Hello");
    }

    @Test
    public void isEmpty() {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>();
        assertTrue(deque.isEmpty());
        deque.addFirst(47);
        assertFalse(deque.isEmpty());
    }

    @Test
    public void size() {
        LinkedListDeque<Double> deque = new LinkedListDeque<>();
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
        LinkedListDeque<Integer> deque = new LinkedListDeque<>(3, 5, 8, 13, 21);
        assertEquals("3 5 8 13 21", deque.toString());
        deque = new LinkedListDeque<>();
        assertEquals("", deque.toString());
    }

    @Test
    public void removeFirst() {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>(3, 5, 8, 13, 21);
        assertEquals((Integer)3, deque.removeFirst());
        assertEquals("5 8 13 21", deque.toString());
        deque = new LinkedListDeque<>();
        assertNull(deque.removeFirst());
    }

    @Test
    public void removeLast() {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>(3, 5, 8, 13, 21);
        assertEquals((Integer)21, deque.removeLast());
        assertEquals("3 5 8 13", deque.toString());
        deque = new LinkedListDeque<>();
        assertNull(deque.removeLast());
    }

    @Test
    public void get() {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>(3, 5, 8, 13, 21);
        assertEquals((Integer)3, deque.get(0));
        assertEquals((Integer)21, deque.get(4));
        assertEquals((Integer)13, deque.get(3));
    }

    @Test
    public void getRecursive() {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>(3, 5, 8, 13, 21);
        assertEquals((Integer)3, deque.getRecursive(0));
        assertEquals((Integer)21, deque.getRecursive(4));
        assertEquals((Integer)13, deque.getRecursive(3));
    }
}