import java.util.ArrayList;
import java.util.List;

public class Trie {

    private TrieNode root; // sentinel node, not containing any meaning full char

    private static final int ALPHABET_SIZE = 26;

    private class TrieNode {
        private char val;
        private TrieNode[] children; // stores chars in numerical order of the char values starting from 'a' to 'z'
        private boolean isEndOfWord;

        private TrieNode(char c) {
            val = c;
            // terminal node: isEndOfWord == true, children will never be null for simplicity
            children = new TrieNode[ALPHABET_SIZE];
            isEndOfWord = false;
        }
    }

    /**
     * Construct a new empty trie.
     */
    public Trie() {
        root = new TrieNode(Character.MIN_VALUE); // sentinel node contains '\u0000' char
    }

    private int indexOf(char c) {
        return c - 'a';
    }

    /* Helper method to insert a char sequence into any trie node */
    private TrieNode insert(CharSequence cs, TrieNode node) {
        if (cs.length() == 0) {
            return node;
        }
        /* at least one char */
        char firstChar = cs.charAt(0);
        if (node == null) {
            node = new TrieNode(firstChar);
        }
        if (cs.length() == 1) {
            // if there is only one character left, then it is the end of the word
            node.isEndOfWord = true;
        }
        /* at least two chars */
        if (cs.length() >= 2) {
            char secondChar = cs.charAt(1);
            int k = indexOf(secondChar);
            // update the k-th child corresponding to this char
            node.children[k] = insert(cs.subSequence(1, cs.length()), node.children[k]);
        }
        return node;
    }

    /**
     * Insert a char sequence into the trie.
     * @param cs
     */
    public void insert(CharSequence cs) {
        root = insert(String.valueOf(Character.MIN_VALUE) + cs, root); // add this dummy char for sentinel node
    }

    /* Retrieves a list of strings that are contained by the sub-trie rooted from this node */
    private List<String> retrieve(TrieNode node) {
        if (node == null) {
            // node cannot be null, otherwise return null
            return null;
        }
        List<String> results = new ArrayList<>();
        String val = node.val == Character.MIN_VALUE ? "" : String.valueOf(node.val);
        if (node.isEndOfWord) {
            // terminal node
            results.add(val);
        }
        for (TrieNode child : node.children) {
            if (child != null) {
                for (String s : retrieve(child)) {
                    results.add(val + s);
                }
            }
        }
        return results;
    }

    /**
     * Returns a list of all strings that this trie contains.
     * @return
     */
    public List<String> retrieveAll() {
        return retrieve(root);
    }

    /* Returns the trie node that hosts the last character of the given prefix, or null of not found. */
    private TrieNode search(CharSequence prefix, TrieNode node) {
        if (node == null || prefix.length() == 0) {
            return null;
        }
        if (prefix.length() == 1) {
            return node;
        }
        return search(prefix.subSequence(1, prefix.length()), node.children[indexOf(prefix.charAt(1))]);
    }

    /**
     * Returns a list of strings that has the given prefix from this trie, or empty list if not found.
     * @param prefix
     * @return
     */
    public List<String> search(CharSequence prefix) {
        List<String> results = retrieve(search(String.valueOf(Character.MIN_VALUE) + prefix, root));
        if (results == null) {
            return new ArrayList<>();
        }
        for (int i = 0; i < results.size(); i++) {
            // note there is an overlapping character between prefix and obtained results
            results.set(i, prefix.subSequence(0, prefix.length() - 1) + results.get(i));
        }
        return results;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : retrieveAll()) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}
