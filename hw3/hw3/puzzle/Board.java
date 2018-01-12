package hw3.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements WorldState {

    private int[][] tiles;
    private int size;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     * @param tiles
     */
    public Board(int[][] tiles) {
        if (tiles == null || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("Input tiles do not form square shape.");
        }
        size = tiles.length;
        this.tiles = new int[size][size];
        int max = size * size - 1; // max allowed tile number
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] > max || tiles[i][j] < 0) {
                    throw new IllegalArgumentException("Illegal tile number: " + tiles[i][j]);
                } else {
                    this.tiles[i][j] = tiles[i][j];
                }
            }
        }
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    /**
     * Returns the board size N
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     * @param i
     * @param j
     * @return
     */
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    /**
     * Helper to calculate distance to goal position for a single tile
     */
    private int distToGoal(int i, int j) {
        int value = tileAt(i, j);
        if (value == 0) {
            return 0;
        }
        int iGoal = (value - 1) / size;
        int jGoal = (value - 1) % size;
        return Math.abs(i - iGoal) + Math.abs(j - jGoal);
    }

    /**
     * Hamming estimate: The number of tiles in the wrong position.
     * @return
     */
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (distToGoal(i, j) != 0) {
                    sum += 1;
                }
            }
        }
        return sum;
    }

    /**
     * Manhattan estimate: The sum of the Manhattan distances (sum of the vertical
     * and horizontal distance) from the tiles to their goal positions.
     * @return
     */
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum += distToGoal(i, j);
            }
        }
        return sum;
    }

    /**
     * Estimated distance to goal. This method should
     * simply return the results of manhattan() as default.
     * @return
     */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    private boolean isInBound(int i, int j) {
        return i >= 0 && i < size && j >= 0 && j < size;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        // idea: swap north, south, west and east tile with the zero tile
        // find the zero tile
        int iZero = -1;
        int jZero = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) == 0) {
                    iZero = i; jZero = j;
                    break;
                }
            }
            if (iZero != -1) {
                break;
            }
        }
        if (iZero == -1) {
            throw new RuntimeException("Cannot find zero (empty) tile.");
        }
        // add neighbor tiles
        List<WorldState> neighbors = new ArrayList<>();
        // following arrays store row and col indices of north, south, west and east tiles
        int[] rowIndices = new int[]{iZero - 1, iZero + 1, iZero, iZero};
        int[] colIndices = new int[]{jZero, jZero, jZero - 1, jZero + 1};
        for (int k = 0; k < rowIndices.length; k++) {
            int i = rowIndices[k];
            int j = colIndices[k];
            if (isInBound(i, j)) {
                Board b = new Board(tiles);
                int val = b.tiles[i][j];
                b.tiles[iZero][jZero] = val;
                b.tiles[i][j] = 0;
                neighbors.add(b);
            }
        }
        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Board)) {
            return false;
        }
        Board b = (Board) o;
        if (b.size() != size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) != b.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Board b = new Board(new int[][]{new int[]{8, 1, 3}, new int[]{4, 0, 2}, new int[]{7, 6, 5}});
        for (WorldState ws : b.neighbors()) {
            System.out.println("Printing neighbor:");
            System.out.println(ws);
        }
    }
}
