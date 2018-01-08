package lab8;

import edu.princeton.cs.algs4.BST;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    private int size;

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private boolean containsKey(K key, Node node) {
        if (key == null || node == null) {
            return false;
        }
        if (key.compareTo(node.key) == 0) {
            return true;
        } else if (key.compareTo(node.key) < 0) {
            return containsKey(key, node.left);
        } else {
            return containsKey(key, node.right);
        }
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    private V get(K key, Node node) {
        if (key == null || node == null) {
            return null;
        }
        if (key.compareTo(node.key) == 0) {
            return node.value;
        } else if (key.compareTo(node.key) < 0) {
            return get(key, node.left);
        } else {
            return get(key, node.right);
        }
    }

    @Override
    public V get(K key) {
        return get(key, root);
    }

    @Override
    public int size() {
        return size;
    }

    private Node put(K key, V value, Node node) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(node.key) == 0) {
            node.value = value;
            return node;
        } else {
            if (key.compareTo(node.key) < 0) {
                node.left = put(key, value, node.left);
            } else {
                node.right = put(key, value, node.right);
            }
            return node;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key value is null.");
        }
        root = put(key, value, root);
    }

    /* not required below */
    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    public static void main(String[] args) {
        BSTMap<Integer, Integer> bst = new BSTMap<>();
        for (int i = 0; i < 47; i++) {
            bst.put(i, i);
        }
        bst.put(5, 55);
        bst.put(10, 100);
        bst.put(47, 6);
        System.out.println("Size = " + bst.size());
        System.out.println("ContainsKey 47? " + bst.containsKey(47));
    }
}
