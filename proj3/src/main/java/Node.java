import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Node class to store node id, latitude, longitude and a list of edges it connects to.
 */
public class Node {
    long id;
    double lat, lon;
    String name;
    List<Edge> edges;

    public Node(long id, double lat, double lon, String name, List<Edge> edges) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.edges = edges;
    }

    public Node(long id, double lat, double lon, String name) {
        this(id, lat, lon, name, new ArrayList<>());
    }

    public Node(long id, double lat, double lon) {
        this(id, lat, lon, null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return ((Long) id).hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
