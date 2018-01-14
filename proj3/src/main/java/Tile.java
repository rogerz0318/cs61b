/**
 * Tile of a quad tree. Stores four children, current depth, name, and longitudinal distance per pixel.
 */
public class Tile implements Comparable<Tile> {
    private Tile[] children;
    private int depth;
    private String name;
    private double ullat, ullon, lrlat, lrlon; // upper left and lower right latitude/longitude
    private double lonDPP;

    public Tile(Tile[] children, int depth, String name,
                 double ullat, double ullon, double lrlat, double lrlon) {
        this.children = children;
        this.depth = depth;
        this.name = name;
        this.ullat = ullat;
        this.ullon = ullon;
        this.lrlat = lrlat;
        this.lrlon = lrlon;
        this.lonDPP = (lrlon - ullon) / MapServer.TILE_SIZE;
    }

    public boolean intersects(double ullat, double ullon, double lrlat, double lrlon) {
        /* Algorithm below is wrong and overly complicated even if implemented correctly. */
        /*
        // is upper left corner in the box?
        boolean ulInBox = this.ullat > lrlat && this.ullat < ullat && this.ullon > ullon && this.ullon < lrlon;
        // is upper right corner in the box?
        boolean urInBox = this.ullat > lrlat && this.ullat < ullat && this.lrlon > ullon && this.lrlon < lrlon;
        // is lower left corner in the box?
        boolean llInBox = this.lrlat > lrlat && this.lrlat < ullat && this.ullon > ullon && this.ullon < lrlon;
        // is lower right corner in the box?
        boolean lrInBox = this.lrlat > lrlat && this.lrlat < ullat && this.lrlon > ullon && this.lrlon < lrlon;
        return ulInBox || urInBox || llInBox || lrInBox;
        */

        /* Instead, consider the situations where they don't intersect. */
        if (getLrlat() > ullat || lrlat > getUllat()) {
            return false;
        }
        if (getUllon() > lrlon || ullon > getLrlon()) {
            return false;
        }
        return true;
    }

    /**
     * This compareTo method for image tiles behaves like this:
     * Tiles with larger latitudes and smaller longitudes are considered smaller
     * in order to sort images from upper left corner to lower right corner.
     * Latitude comparison dominates over longitude comparison.
     * @param o
     * @return
     */
    @Override
    public int compareTo(Tile o) {
        if (this == o) {
            return 0;
        }
        if (ullat < o.ullat) {
            return 1; // higher latitude tile is smaller
        } else if (ullat > o.ullat) {
            return -1;
        } else {
            if (ullon < o.ullon) {
                return -1;
            } else if (ullon > o.ullon) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public Tile[] getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }

    public String getName() {
        return name;
    }

    public double getUllat() {
        return ullat;
    }

    public double getUllon() {
        return ullon;
    }

    public double getLrlat() {
        return lrlat;
    }

    public double getLrlon() {
        return lrlon;
    }

    public double getLonDPP() {
        return lonDPP;
    }
}