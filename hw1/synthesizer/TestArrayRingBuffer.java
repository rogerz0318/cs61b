package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(4);
        assertTrue(arb.isEmpty());
        assertEquals(4, arb.capacity());
        arb.enqueue(1);
        assertFalse(arb.isEmpty());
        assertEquals(1, arb.fillCount());
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);
        assertTrue(arb.isFull());
        assertEquals(1, arb.peek());
        assertEquals(1, arb.dequeue());
        assertEquals(3, arb.fillCount());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
