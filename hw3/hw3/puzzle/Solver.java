package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    private MinPQ<Node> pq;
    private int moves;
    private List<WorldState> route;

    private class Node implements Comparable {

        private WorldState state;
        private int distTo;
        private Node prev;
        private int priority;

        public Node(WorldState state, int distTo, Node prev) {
            this.state = state;
            this.distTo = distTo;
            this.prev = prev;
            priority = distTo + state.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Node) {
                return priority - ((Node) o).priority;
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     * @param initial
     */
    public Solver(WorldState initial) {
        pq = new MinPQ<>();
        pq.insert(new Node(initial, 0, null));
        Node min;
        // A* algorithm
        while (!(min = pq.delMin()).state.isGoal()) {
            for (WorldState neighbor : min.state.neighbors()) {
                if (min.prev == null || !neighbor.equals(min.prev.state)) {
                    // if neighbor state is not the same as previous state, enqueue it to priority queue
                    pq.insert(new Node(neighbor, min.distTo + 1, min));
                }
            }
        }
        // found goal, record the route, from goal to origin
        route = new ArrayList<>();
        Node node = min;
        while (node != null) {
            route.add(0, node.state);
            node = node.prev;
        }
        moves = route.size() - 1;
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     * @return
     */
    public int moves() {
        return moves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     * @return
     */
    public Iterable<WorldState> solution() {
        return route;
    }
}