/**
 * Array implementation of Deque
 */
public class ArrayDeque<Item> implements Deque<Item> {

    private static final int INITIAL_CAPACITY = 8;
    private static final double UTILIZATION_THRESHOLD = 0.25;
    private Item[] items;
    private int size = 0;
    private int firstIndex = 0; // first index is defined to be right at the front most item
    private int lastIndex = 0; // last index is defined to be 1 larger than the index of back most item

    /**
     * Create an array deque with initial capacity of 8.
     */
    public ArrayDeque() {
        items = (Item[])new Object[INITIAL_CAPACITY];
    }

    /**
     * Create an array deque with specified initial capacity.
     * @param capacity Initial capacity
     */
    public ArrayDeque(int capacity) {
        items = (Item[])new Object[capacity];
    }

    /**
     * Create an array deque with many number of items.
     * @param items
     */
    public ArrayDeque(Item... items) {
        this();
        for(Item item: items) {
            this.addLast(item);
        }
    }

    @Override
    public void addFirst(Item t) {
        expandIfNeeded();
        firstIndex = prevIndex(firstIndex);
        items[firstIndex] = t;
        size += 1;
    }

    @Override
    public void addLast(Item t) {
        expandIfNeeded();
        items[lastIndex] = t;
        lastIndex = nextIndex(lastIndex);
        size += 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < size; i++) {
            s.append(get(i).toString()).append(" ");
        }
        return s.toString().trim();
    }

    @Override
    public void printDeque() {
        System.out.println(toString());
    }

    @Override
    public Item removeFirst() {
        if (size == 0) return null;
        else {
            Item t = items[firstIndex];
            items[firstIndex] = null;
            firstIndex = nextIndex(firstIndex);
            size -= 1;
            shrinkIfNeeded();
            return t;
        }
    }

    @Override
    public Item removeLast() {
        if (size == 0) return null;
        else {
            lastIndex = prevIndex(lastIndex);
            Item t = items[lastIndex];
            items[lastIndex] = null;
            size -= 1;
            shrinkIfNeeded();
            return t;
        }
    }

    @Override
    public Item get(int index) {
        return items[findIndex(firstIndex, index)];
    }

    /**
     * Expand the size of items array when it is full.
     */
    private void expandIfNeeded() {
        if (size == items.length) {
            Item[] newItems = (Item[]) new Object[items.length * 2]; // expand to 2x space
            System.arraycopy(items, firstIndex, newItems, 0, items.length - firstIndex);
            System.arraycopy(items, 0, newItems, items.length - firstIndex, lastIndex);
            items = newItems;
            firstIndex = 0;
            lastIndex = size;
        }
    }

    /**
     * Shrink the size of items array when utilization is smaller than 0.25.
     */
    private void shrinkIfNeeded() {
        while (items.length >= 2 * INITIAL_CAPACITY && utilization() < UTILIZATION_THRESHOLD) {
            // shrink to utilization ~ 2 * threshold, or the initial capacity, whichever is larger.
            int newSize = Math.max(Math.max((int)(size / (UTILIZATION_THRESHOLD * 2)), INITIAL_CAPACITY), size);
//            System.out.println("size = " + size);
//            System.out.println("newSize = " + newSize);
            Item[] newItems = (Item[]) new Object[newSize];
            if (firstIndex > lastIndex) {
                // data are in two segments
                System.arraycopy(items, firstIndex, newItems, 0, items.length - firstIndex);
                System.arraycopy(items, 0, newItems, items.length - firstIndex, lastIndex);
            } else if (firstIndex < lastIndex) {
                // data is continuous
                System.arraycopy(items, firstIndex, newItems, 0, size);
            } else {
                // empty array, do nothing
            }
            items = newItems;
            firstIndex = 0;
            lastIndex = size;
        }
    }

    /**
     * Calculates the utilization of the items array.
     * @return Utilization factor, ranging from 0 to 1, indicating the fraction of used spaces in items array.
     */
    private double utilization() {
        return (double)size / items.length;
    }

    /**
     * Helper function to calculate index in front of given index, assuming the array is a loop structure.
     * @param index Given index
     * @return Index in front of given index
     */
    private int prevIndex(int index) {
        return findIndex(index, -1);
    }

    /**
     * Helper function to calculate index next to given index, assuming the array is a loop structure.
     * @param index Given index
     * @return Index next to given index
     */
    private int nextIndex(int index) {
        return findIndex(index, 1);
    }

    /**
     * A general index stepping function that calculates index with arbitrary offset.
     * @param initial Initial index
     * @param offset Offset from given index
     * @return New index offset from the given one
     */
    private int findIndex(int initial, int offset) {
        int i = (initial + offset) % items.length;
        return i < 0 ? i + items.length : i;
    }

//    public int getArrayLength() {
//        return items.length;
//    }

    public static void main(String[] args) {
        ArrayDeque<Integer> deque = new ArrayDeque<>(3, 5, 8, 13, 21);
        for (int i = 0; i < 5; i++) deque.addFirst(1);
        for (int i = 0; i < 50; i++) deque.addLast(2);
        for (int i = 0; i < 9; i++) deque.removeFirst();
        for (int i = 0; i < 45; i++) deque.removeLast();
    }
}
