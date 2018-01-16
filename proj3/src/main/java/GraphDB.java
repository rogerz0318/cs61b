import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private Map<Long, Node> nodes;
    private Map<String, Node> pointOfInterests;

    public GraphDB() {
        nodes = new HashMap<>();
        pointOfInterests = new HashMap<>();
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        this();
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
//        System.out.println("Number of nodes before cleaning: " + nodes.keySet().size());
        Iterator<Long> it = vertices().iterator();
        while (it.hasNext()) {
            long id = it.next();
            if (getNode(id).edges.isEmpty()) {
                it.remove();
            }
        }
//        System.out.println("Number of nodes after cleaning: " + nodes.keySet().size());
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        List<Long> results = new ArrayList<>();
        Node node = getNode(v);
        for (Edge e : node.edges) {
            results.add(e.adjacent(node).id);
        }
        return results;
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        Node n1 = getNode(v), n2 = getNode(w);
        return distance(n1, n2);
    }

    static double distance(Node n1, Node n2) {
        return GraphDB.distance(n1.lon, n2.lon, n1.lat, n2.lat);
    }

    static double distance(double lon1, double lon2, double lat1, double lat2) {
        double dlon = lon1 - lon2, dlat = lat1 - lat2;
        return Math.sqrt(dlon * dlon + dlat * dlat);
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        return closestNode(lon, lat).id;
    }

    Node closestNode(double lon, double lat) {
        Node closestNode = null;
        double closestDistance = Double.MAX_VALUE;
        for (Node node : nodes.values()) {
            double distance = distance(node.lon, lon, node.lat, lat);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestNode = node;
            }
        }
        return closestNode;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        return getNode(v).lon;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        return getNode(v).lat;
    }

    /**
     * Return node by id.
     * @param id
     * @return
     */
    Node getNode(long id) {
        return nodes.get(id);
    }

    /**
     * Add node by all the node information.
     * @param id
     * @param lat
     * @param lon
     * @param name
     */
    void addNode(long id, double lat, double lon, String name) {
        addNode(new Node(id, lat, lon, name));
    }

    /**
     * Add node by node instance.
     * @param node
     */
    void addNode(Node node) {
        nodes.put(node.id, node);
    }

    /**
     * Add node to points of interest.
     * @param node
     */
    void addPointOfInterest(Node node) {
        pointOfInterests.put(node.name, node);
    }

    /**
     * Add edge to the graph, and record this edge in connected nodes.
     * @param v
     * @param w
     * @param tags
     */
    void addEdge(long v, long w, Map<String, String> tags) {
        Node n1 = getNode(v), n2 = getNode(w);
        Edge e = new Edge(n1, n2, tags);
        n1.edges.add(e);
        n2.edges.add(e);
    }
}
