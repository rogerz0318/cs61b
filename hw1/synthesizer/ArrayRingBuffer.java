package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        this.capacity = capacity;
        fillCount = 0; first = 0; last = 0;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (isFull())
            throw new RuntimeException("Ring buffer overflow");
        else {
            rb[last] = x;
            last = (last + 1) % capacity;
            fillCount += 1;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        if (isEmpty())
            throw new RuntimeException("Ring buffer underflow");
        else {
            T t = rb[first];
            rb[first] = null;
            first = (first + 1) % capacity;
            fillCount -= 1;
            return t;
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        return rb[first];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return !isEmpty();
        }

        @Override
        public T next() {
            return dequeue();
        }
    }

    public static void main(String[] args) {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(5);
        for (int i = 0; i < arb.capacity; i++)
            arb.enqueue(i);
        System.out.println(arb.isEmpty());
        for (int i : arb)
            System.out.println(i);
        System.out.println(arb.isEmpty());
    }
}
