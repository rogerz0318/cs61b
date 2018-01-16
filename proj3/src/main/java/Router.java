import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    private static final String mode = "special";

    /**
     * Return a LinkedList of <code>Long</code>s representing the shortest path from st to dest, 
     * where the longs are node IDs.
     */
    public static LinkedList<Long> shortestPath(GraphDB g, double stlon, double stlat, double destlon, double destlat) {
        if (mode.equals("comparison")) {
            /* comparison on my MacBook Air 2013 shows that the two algorithms are in general comparable,
             * but special PQ version that uses ArrayHeap usually takes slightly less time.
             */
            Long startTime;
            startTime = System.nanoTime();
            LinkedList<Long> l1 = shortestPathJavaPQ(g, stlon, stlat, destlon, destlat);
            Long javaPQTime = System.nanoTime() - startTime;
            startTime = System.nanoTime();
            LinkedList<Long> l2 = shortestPathSpecialPQ(g, stlon, stlat, destlon, destlat);
            Long specialPQTime = System.nanoTime() - startTime;
            System.out.println("Java PQ: " + javaPQTime + " ns, Special PQ: " + specialPQTime + " ns");
            if (!l1.equals(l2)) {
                throw new RuntimeException("Inconsistent results between two A* algorithms.");
            }
            return l1;
        } else if (mode.equals("java")) {
            return shortestPathJavaPQ(g, stlon, stlat, destlon, destlat);
        } else if (mode.equals("special")) {
            return shortestPathSpecialPQ(g, stlon, stlat, destlon, destlat);
        }
        throw new RuntimeException("Illegal mode: " + mode);
    }

    /* A* algorithm using Java's priority queue, dependent on PQNode helper class */
    private static LinkedList<Long> shortestPathJavaPQ(GraphDB g, double stlon, double stlat,
                                                      double destlon, double destlat) {
        LinkedList<Long> path = new LinkedList<>();
        PriorityQueue<PQNode> pq = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();
        // set the stage
        final Node start = g.closestNode(stlon, stlat), dest = g.closestNode(destlon, destlat);
        final double initDist = GraphDB.distance(start, dest);
        pq.add(new PQNode(start, null, 0, initDist));
        boolean routeFound = false;
        PQNode root = null;
        // priority queue operations
        while (!pq.isEmpty()) {
            root = pq.poll();
            visited.add(root.node);
//            System.out.println(String.format("Current node id = %d, distTo = %.5f, remaining = %.5f",
//                    root.node.id, root.distTo, GraphDB.distance(root.node, dest)));
            // found destination
            if (root.node.equals(dest)) {
                routeFound = true;
                break;
            }
            // not yet found, investigate neighbors
            for (Edge e : root.node.edges) {
                Node adj = e.adjacent(root.node);
                if (!visited.contains(adj)) {
                    double distTo = root.distTo + e.dist;
                    PQNode n = new PQNode(adj, root, distTo, distTo + GraphDB.distance(adj, dest));
                    // enqueue n
                    pq.add(n); // not necessary performance penalty here, see
                    // https://www.redblobgames.com/pathfinding/a-star/implementation.html
                }
            }
        }
        // assemble results
        if (routeFound) {
            // if route found, root is the destination with previous node chains
            while (root != null) {
                path.add(0, root.node.id);
                root = root.prev;
            }
        }
//        System.out.println("Visited: " + visited);
//        System.out.println("Path found: " + path);
        return path;
    }

    /* another version, no need for wrapper class but need to use special priority queue
     * that supports modification of priority (uses ArrayHeap from Lab 10) */
    private static LinkedList<Long> shortestPathSpecialPQ(GraphDB g, double stlon, double stlat,
                                                         double destlon, double destlat) {
        LinkedList<Long> path = new LinkedList<>();
        ArrayHeap<Node> pq = new ArrayHeap<>();
        HashMap<Node, Double> distTo = new HashMap<>();
        HashMap<Node, Edge> edgeTo = new HashMap<>();
        // set the stage
        final Node start = g.closestNode(stlon, stlat), dest = g.closestNode(destlon, destlat);
        final double initDist = GraphDB.distance(start, dest);
        pq.insert(start, initDist);
        distTo.put(start, 0.0);
        boolean routeFound = false;
        Node root = null;
        // priority queue operations
        while (!pq.isEmpty()) {
            root = pq.removeMin();
            // found destination
            if (root.equals(dest)) {
                routeFound = true;
                break;
            }
            // not yet found, investigate neighbors
            for (Edge e : root.edges) {
                Node adj = e.adjacent(root);
                double newDistTo = distTo.get(root) + e.dist;
                double newPriority = newDistTo + GraphDB.distance(adj, dest);
                if (!distTo.containsKey(adj)) {
                    // if this neighbor has never been seen
                    distTo.put(adj, newDistTo);
                    edgeTo.put(adj, e);
                    pq.insert(adj, newPriority);
                } else {
                    // it has been seen, may or may not in priority queue
                    if (distTo.get(adj) > newDistTo) {
                        // if the new distance is smaller (better), update the recorded distTo and edgeTo
                        distTo.put(adj, newDistTo);
                        edgeTo.put(adj, e);
                        if (pq.contains(adj)) {
                            pq.changePriority(adj, newPriority);
                        } else {
                            pq.insert(adj, newPriority);
                        }
                    }
                }
            }
        }
        // assemble results
        if (routeFound) {
            // if route found, root is the destination node
            path.add(root.id);
            while (edgeTo.containsKey(root)) {
                root = edgeTo.get(root).adjacent(root);
                path.add(0, root.id);
            }
        }
        return path;
    }

    private static class PQNode implements Comparable<PQNode> {
        private Node node;
        private PQNode prev;
        private double distTo;
        private double priority;

        private PQNode(Node node, PQNode prev, double distTo, double priority) {
            this.node = node;
            this.prev = prev;
            this.distTo = distTo;
            this.priority = priority;
        }

        @Override
        public int compareTo(PQNode o) {
            if (priority > o.priority) {
                return 1;
            } else if (priority < o.priority) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof PQNode)) {
                return false;
            }
            PQNode pqn = (PQNode) obj;
            return node.equals(pqn.node) && priority == pqn.priority;
        }

        @Override
        public int hashCode() {
            return node.hashCode() * 31 + ((Double) priority).hashCode();
        }
    }
}
