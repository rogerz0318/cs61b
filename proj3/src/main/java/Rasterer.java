import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private QuadTree tree;
    private String imgRoot;

    private static final int MAX_DEPTH = 7;

    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        this.imgRoot = imgRoot;
        tree = new QuadTree(MAX_DEPTH);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     * @see MapServer
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
//        System.out.println(params);
//        long startTime = System.nanoTime();
        Map<String, Object> results = new HashMap<>();
        /* REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat", "lrlon", "w", "h"}; */
        double ullat = params.get("ullat"), ullon = params.get("ullon");
        double lrlat = params.get("lrlat"), lrlon = params.get("lrlon");
        double w = params.get("w"), h = params.get("h");
        double lonDPP = (lrlon - ullon) / w;
        // initialize results variables
        String[][] grid = null;
        double rasterUllon = 0, rasterUllat = 0, rasterLrlon = 0, rasterLrlat = 0;
        int depth = 0;
        boolean query_success;

        try {
            List<Tile> tiles = tree.sortedIntersectingTiles(lonDPP, ullat, ullon, lrlat, lrlon);
            Tile first = tiles.get(0);
            Tile last = tiles.get(tiles.size() - 1);

            rasterUllon = first.getUllon();
            rasterUllat = first.getUllat();
            rasterLrlon = last.getLrlon();
            rasterLrlat = last.getLrlat();
            depth = first.getDepth();

            if (tiles.size() == 1) {
                grid = new String[][]{new String[]{imgRoot + "root.png"}};
            } else {
                // use the first and last tile to determine number of rows and columns
                double rasterLonDPP = first.getLonDPP();
                int nCol = (int) Math.round((rasterLrlon - rasterUllon) / rasterLonDPP / MapServer.TILE_SIZE);
                if (tiles.size() % nCol != 0) {
                    throw new RuntimeException("Intersecting tiles are not square shape: " +
                            "size is " + tiles.size() + ", but number of column is calculated to be " + nCol);
                }
                int nRow = tiles.size() / nCol;

                grid = new String[nRow][nCol];
                Iterator<Tile> it = tiles.iterator();
                for (int i = 0; i < nRow; i++) {
                    for (int j = 0; j < nCol; j++) {
                        grid[i][j] = imgRoot + it.next().getName() + ".png";
                    }
                }
            }
            query_success = true;
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
//            ex.printStackTrace();
            query_success = false;
        }

        // assemble results
        results.put("render_grid", grid);
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_lr_lat", rasterLrlat);
        results.put("depth", depth);
        results.put("query_success", query_success);

//        long endTime = System.nanoTime();
//        System.out.println("getMapRaster execution time: " + (double) (endTime - startTime) + " ns");
        return results;
    }
}
