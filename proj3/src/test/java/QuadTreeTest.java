import org.junit.Test;
import static org.junit.Assert.*;

public class QuadTreeTest {
    @Test
    public void tilesAtDepthTest() {
        QuadTree tree = new QuadTree(7);
        assertEquals("[1, 2, 3, 4]", tree.tilesAtDepth(1).toString());
        assertEquals("[11, 12, 13, 14, 21, 22, 23, 24, 31, 32, 33, 34, 41, 42, 43, 44]",
                tree.tilesAtDepth(2).toString());
    }

    @Test
    public void sortedIntersectingTilesTest() {
        QuadTree tree = new QuadTree(7);
        assertEquals("[]",
                tree.sortedIntersectingTiles(1, MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON,
                        MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON).toString());
        assertEquals("[1, 2, 3, 4]",
                tree.sortedIntersectingTiles((MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 257,
                        MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON,
                        MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON).toString());
        assertEquals("[11, 12, 21, 22, 13, 14, 23, 24, 31, 32, 41, 42, 33, 34, 43, 44]",
                tree.sortedIntersectingTiles((MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 513,
                        MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON,
                        MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON).toString());
        double lrlon=-122.2200738143921, ullon=-122.27625, w=1309.0, h=605.0, ullat=37.88, lrlat=37.85950056943425;
        assertEquals("[123, 124, 213, 214, 223, 224, 141, 142, 231, 232, " +
                        "241, 242, 143, 144, 233, 234, 243, 244]",
                tree.sortedIntersectingTiles((lrlon - ullon) / w, ullat, ullon, lrlat, lrlon).toString());
    }
}