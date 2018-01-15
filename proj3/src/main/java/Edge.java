import java.util.Map;

/**
 * Edge class that stores two nodes at its both ends, the distance (length) of this edge, and related tags.
 */
public class Edge {
    Node n1, n2;
    double dist;
    Map<String, String> tags;

    public Edge(Node n1, Node n2, double dist, Map<String, String> tags) {
        this.n1 = n1;
        this.n2 = n2;
        this.dist = dist;
        this.tags = tags;
    }

    public Edge(Node n1, Node n2, Map<String, String> tags) {
        this(n1, n2, GraphDB.distance(n1, n2), tags);
    }

    /**
     * Returns the node that is on the other end to the given node.
     * Returns null if given node is not associated with this edge.
     * @param node
     * @return
     */
    public Node adjacent(Node node) {
        if (node == n1) {
            return n2;
        }
        if (node == n2) {
            return n1;
        }
        return null;
    }
}