/**
 * Linked list implementation of Deque
 */
public class LinkedListDeque<Item> implements Deque<Item> {

    /**
     * Helper class that stores the item and references to the Node at previous and next Deque index
     */
    private class Node { // no need to use generic type for this private class!
        public Node prev, next;
        public Item item;

        public Node(Item item, Node prev, Node next) {
            this.prev = prev;
            this.next = next;
            this.item = item;
        }
    }

    private Node sentinel;
    private int size = 0;

    /**
     * Creates an empty linked list deque.
     */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /**
     * Create a linked list deque with many number of items.
     * @param items
     */
    public LinkedListDeque(Item... items) {
        this();
        for(Item item: items) {
            this.addLast(item);
        }
    }

    @Override
    public void addFirst(Item t) {
        Node firstNode = sentinel.next;
        Node node = new Node(t, sentinel, firstNode);
        firstNode.prev = node;
        sentinel.next = node;
        size += 1;
    }

    @Override
    public void addLast(Item t) {
        Node lastNode = sentinel.prev;
        Node node = new Node(t, lastNode, sentinel);
        lastNode.next = node;
        sentinel.prev = node;
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
        Node node = sentinel.next;
        for (int i = 0; i < size; i++) {
            s.append(node.item.toString()).append(" ");
            node = node.next;
        }
        return s.toString().trim();
    }

    @Override
    public void printDeque() {
        System.out.println(toString());
    }

    @Override
    public Item removeFirst() {
        if (size == 0) {
            return null;
        } else {
            Node firstNode = sentinel.next;
            firstNode.next.prev = sentinel;
            sentinel.next = firstNode.next;
            size -= 1;
            return firstNode.item;
        }
    }

    @Override
    public Item removeLast() {
        if (size == 0) {
            return null;
        } else {
            Node lastNode = sentinel.prev;
            lastNode.prev.next = sentinel;
            sentinel.prev = lastNode.prev;
            size -= 1;
            return lastNode.item;
        }
    }

    @Override
    public Item get(int index) {
        if (index >= size || index < 0) return null;
        Node node = sentinel.next;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.item;
    }

    /**
     * Same as get, but uses recursion.
     * @param index Index of item
     * @return Item at specified index
     */
    public Item getRecursive(int index) {
        return getRecursiveFrom(index, sentinel.next);
    }

    /* Helper method for getRecursive(int index) */
    private Item getRecursiveFrom(int index, Node node) {
        if (index == 0) return node.item;
        else if (index > 0) return getRecursiveFrom(index-1, node.next);
        else return null;
    }
}
