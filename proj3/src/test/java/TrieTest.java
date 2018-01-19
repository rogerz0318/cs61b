import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TrieTest {

    private Trie t;

    @Before
    public void setUp() {
        t = new Trie();
        t.insert("apple");
        t.insert("apple");
        t.insert("app");
        t.insert("apartment");
        t.insert("pear");
    }

    @Test
    public void insertTest() {
        assertEquals("apartment\napp\napple\npear\n", t.toString());
    }

    @Test
    public void searchTest() {
        assertEquals("[app, apple]", t.search("app").toString());
        assertEquals("[]", t.search("apps").toString());
    }
}