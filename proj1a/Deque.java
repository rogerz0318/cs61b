/**
 * Double ended queue interface.
 */
public interface Deque<Item> {
    /**
     * Adds an item to the front of the Deque.
     * @param t Item to be added
     */
    void addFirst(Item t);

    /**
     * Adds an item to the back of the Deque.
     * @param t Item to be added
     */
    void addLast(Item t);

    /**
     * Returns true if deque is empty, false otherwise.
     * @return true if empty, false if not
     */
    boolean isEmpty();

    /**
     * Returns the number of items in the Deque.
     * @return number of items
     */
    int size();

    /**
     * Prints the items in the Deque from first to last, separated by a space.
     */
    void printDeque();

    /**
     * Removes and returns the item at the front of the Deque. If no such item exists, returns null.
     * @return Item at the front of the queue that is removed, or null
     */
    Item removeFirst();

    /**
     * Removes and returns the item at the back of the Deque. If no such item exists, returns null.
     * @return Item at the back of the queue that is removed, or null
     */
    Item removeLast();

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * @param index Index of item
     * @return Item at specified index, or null
     */
    Item get(int index);
}
