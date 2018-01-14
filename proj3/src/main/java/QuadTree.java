import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This QuadTree is a tree in which each node (tile) has exactly four children.
 * It is designed specifically to represent raster image tree structure:
 * children are higher-resolution sub-tiles of parent.
 */
public class QuadTree {

    private Tile root;
    private int maxDepth;

    /**
     * Constructs a QuadTree with given max depth. Root counts as depth zero.
     * @param maxDepth
     */
    public QuadTree(int maxDepth) {
        this.maxDepth = maxDepth;
        root = buildTile(0, "", MapServer.ROOT_ULLAT, MapServer.ROOT_ULLON,
                MapServer.ROOT_LRLAT, MapServer.ROOT_LRLON);
    }

    /* Recursive helper method to generate tile at given depth. */
    private Tile buildTile(int depth, String name,
                           double ullat, double ullon, double lrlat, double lrlon) {
        if (depth == maxDepth) {
            return new Tile(null, depth, name, ullat, ullon, lrlat, lrlon);
        }
        Tile[] children = new Tile[4];
        int newDepth = depth + 1;
        for (int i = 0; i < 4; i++) {
            String newName = name + (i + 1);
            double centerLat = (ullat + lrlat) / 2, centerLon = (ullon + lrlon) / 2;
            double newUllat = ullat, newUllon = ullon, newLrlat = lrlat, newLrlon = lrlon;
            // calculate new latitude and longitude per tile
            if (i == 0) {
                // upper left tile
                newLrlat = centerLat; newLrlon = centerLon;
            } else if (i == 1) {
                // upper right tile
                newUllon = centerLon; newLrlat = centerLat;
            } else if (i == 2) {
                // lower left tile
                newUllat = centerLat; newLrlon = centerLon;
            } else {
                // lower right tile
                newUllat = centerLat; newUllon = centerLon;
            }
            children[i] = buildTile(newDepth, newName, newUllat, newUllon, newLrlat, newLrlon);
        }
        return new Tile(children, depth, name, ullat, ullon, lrlat, lrlon);
    }

    /**
     * Returns a sorted list of tiles that intersects with the given query box
     * (specified by upper left and lower right latitudes/longitudes)
     * @param lonDPP
     * @param ullat
     * @param ullon
     * @param lrlat
     * @param lrlon
     * @return
     */
    public List<Tile> sortedIntersectingTiles(double lonDPP, double ullat, double ullon, double lrlat, double lrlon) {
        List<Tile> results = intersectingTiles(root, lonDPP, ullat, ullon, lrlat, lrlon);
        results.sort(Comparator.naturalOrder()); // this should be k*log(k) time complexity
        return results;
    }

    /* Helper method that returns all intersecting child tiles of a given parent. */
    private List<Tile> intersectingTiles(Tile parent, double lonDPP, double ullat, double ullon,
                                         double lrlat, double lrlon) {
        List<Tile> results = new ArrayList<>();
        if (parent.getLonDPP() <= lonDPP || parent.getDepth() == maxDepth) {
            // reached the lonDPP target, or already at leaf
            results.add(parent); // parent already determined that this child intersects with box
            return results;
        }
        for (Tile child : parent.getChildren()) {
            if (child.intersects(ullat, ullon, lrlat, lrlon)) {
                // add child to the list only if it intersects with query box
                results.addAll(intersectingTiles(child, lonDPP, ullat, ullon, lrlat, lrlon));
            }
        }
        return results;
    }

    /**
     * Returns a list of tiles at given depth.
     * @param depth
     * @return
     */
    public List<Tile> tilesAtDepth(int depth) {
        return tilesAtDepth(root, depth);
    }

    /* Recursive helper method that returns a list of tiles at given depth */
    private List<Tile> tilesAtDepth(Tile tile, int depth) {
        List<Tile> results = new ArrayList<>();
        if (depth == tile.getDepth()) {
            results.add(tile);
            return results;
        }
        for (Tile child : tile.getChildren()) {
            results.addAll(tilesAtDepth(child, depth));
        }
        return results;
    }

    public static void main(String[] args) {
        QuadTree tree = new QuadTree(7);
        double lrlon=-122.2200738143921, ullon=-122.27625, w=1309.0, h=605.0, ullat=37.88, lrlat=37.85950056943425;
//        double lrlon=MapServer.ROOT_LRLON, ullon=MapServer.ROOT_ULLON, w=1309.0, h=605.0,
//                ullat=MapServer.ROOT_ULLAT, lrlat=MapServer.ROOT_LRLAT;
        System.out.println(tree.sortedIntersectingTiles((lrlon - ullon) / w, ullat, ullon, lrlat, lrlon));
    }
}
