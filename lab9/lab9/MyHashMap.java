package lab9;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

public class MyHashMap<K, V> implements Map61B<K, V> {

    private HashSet<K> keys;
    private ArrayList<Entry>[] lists;
    private int capacity;
    private int size;
    private double loadFactor;

    private class Entry {
        private K key;
        private V val;

        private Entry(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public MyHashMap() {
        this(16); // initial capacity of 16 is what Java HashMap uses
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75); // load factor of 0.75 is what Java HashMap uses
    }

    public MyHashMap(int initialSize, double loadFactor) {
        keys = new HashSet<>(initialSize);
        lists = new ArrayList[initialSize];
        capacity = initialSize;
        for (int i = 0; i < initialSize; i++) {
            lists[i] = new ArrayList<Entry>();
        }
        size = 0;
        this.loadFactor = loadFactor;
    }

    @Override
    public void clear() {
        keys.clear();
        for (List valList : lists) {
            valList.clear();
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % capacity; // from Algorithm, 4th Edition
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        for (Entry e : lists[hash(key)]) {
            if (e.key.equals(key)) {
                return e.val;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private void resize(int newCapacity) {
        capacity = newCapacity;
        ArrayList<Entry>[] newLists = new ArrayList[capacity];
        // initialize new lists
        for (int i = 0; i < capacity; i++) {
            newLists[i] = new ArrayList<>();
        }
        // rehash and store entries to new lists
        for (ArrayList<Entry> list : lists) {
            for (Entry e : list) {
                newLists[hash(e.key)].add(e);
            }
        }
        // replace fields
        lists = newLists;
    }

    @Override
    public void put(K key, V value) {
        if ((double) (size + 1) / capacity > loadFactor) {
            resize(2 * capacity);
        }
        // update value if key already exists
        ArrayList<Entry> list = lists[hash(key)];
        for (Entry e : list) {
            if (e.key.equals(key)) {
                e.val = value;
                return;
            }
        }
        // otherwise, add a new entry
        keySet().add(key);
        list.add(new Entry(key, value));
        size++;
    }

    @Override
    public Set<K> keySet() {
        return keys;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<K> it = iterator();
        while (it.hasNext()) {
            K key = it.next();
            V val = get(key);
            sb.append(key.toString()).append(" : ").append(val.toString()).append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] keys = new String[]{"Spring", "Summer", "Fall", "Winter"};
        Double[] vals = new Double[]{10.8, 25.6, 12.7, -3.5};
        MyHashMap<String, Double> t = new MyHashMap<>();
        for (int i = 0; i < keys.length; i++) {
            t.put(keys[i], vals[i]);
        }
        System.out.println(t);
    }
}
