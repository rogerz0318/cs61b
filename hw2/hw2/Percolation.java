package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    /**
     * About status:
     * flattened grid. 0: blocked, 1: open, 2: component is rooted (reachable from bottom),
     * 3: component is full (reachable from top), 4: component is percolative
     * (reachable from both top and bottom).
     */
    private int[] status;
    private WeightedQuickUnionUF wqu; // flattened grid
    private int count;
    private int dimension;
    private boolean percolating;

    /**
     * create N-by-N grid, with all sites initially blocked
     * @param N
     */
    public Percolation(int N)    {
        if (N <= 0) {
            throw new IllegalArgumentException("Dimension of grid must be larger than zero.");
        }
        status = new int[N * N]; // by default, all status will be initialized to zero (blocked)
        wqu = new WeightedQuickUnionUF(N * N); // initially none connected
        dimension = N;
        count = 0;
        percolating = false;
    }

    /**
     * Helper method to convert index. Note that row and col starts from zero.
     * Returns -1 for row and col combination that is out of bounds.
     * @param row
     * @param col
     */
    private int index(int row, int col) {
        int k = row * dimension + col;
        if (k < 0 || k >= status.length) {
            return -1;
        }
        return k;
    }

    /**
     * Following are helper methods to get index of north, south, west and east neighbors.
     * They will throw IndexOutOfBounds exceptions.
     */
    private int north(int row, int col) {
        return index(row - 1, col);
    }

    private int south(int row, int col) {
        return index(row + 1, col);
    }

    private int west(int row, int col) {
        return index(row, col - 1);
    }

    private int east(int row, int col) {
        return index(row, col + 1);
    }

    /**
     * Helper method that returns an array of neighbor indices.
     * @param row
     * @param col
     * @return
     */
    private int[] neighbors(int row, int col) {
        return new int[]{north(row, col), south(row, col), west(row, col), east(row, col)};
    }

    private void makeOpen(int index) {
        status[index] = 1;
    }

    private void makeRooted(int index) {
        status[wqu.find(index)] = 2;
    }

    private void makeFull(int index) {
        status[wqu.find(index)] = 3;
    }

    private void makePercolating(int index) {
        status[wqu.find(index)] = 4;
    }

    /**
     * open the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        int self = index(row, col);
        if (isOpen(self)) {
            return;
        }
        makeOpen(self);
        count++;
        // if any of neighbors is open, make them connected, and record if self or any neighbor is full or rooted.
        boolean anyFull = row == 0;
        boolean anyRooted = row == dimension - 1;
        for (int neighbor : neighbors(row, col)) {
            if (neighbor != -1 && isOpen(neighbor)) {
                anyFull = anyFull || isFull(neighbor);
                anyRooted = anyRooted || isRooted(neighbor);
                wqu.union(self, neighbor);
            }
        }
        // after the union:
        // check if the union component is percolating:
        // if this site and neighbors are both full and rooted, then it percolates.
        if (anyFull && anyRooted) {
            makePercolating(self);
            percolating = true;
        }
        // if this site or any of neighbors is previously full, mark the union component as full.
        else if (anyFull) {
            makeFull(self);
        }
        // if this site or any of neighbors is previous rooted, mark the union as rooted.
        else if (anyRooted) {
            makeRooted(self);
        }
        // otherwise do nothing, just keep it open
    }

    private boolean isOpen(int index) {
        return status[index] > 0;
    }

    private boolean isRooted(int index) {
        int st = status[wqu.find(index)];
        return st == 2 || st == 4;
    }

    private boolean isFull(int index) {
        return status[wqu.find(index)] > 2;
    }

    /**
     * is the site (row, col) open?
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        return isOpen(index(row, col));
    }
    /**
     * is the site (row, col) full?
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        return isFull(index(row, col));
    }

    /**
     * number of open sites
     * @return
     */
    public int numberOfOpenSites() {
        return count;
    }

    /**
     * does the system percolate?
     * @return
     */
    public boolean percolates() {
        return percolating;
    }

    private void print() {
        for (int i = 0; i < status.length; i++) {
            if (i != 0 && i % dimension == 0) {
                System.out.printf("\n");
            }
            System.out.printf(status[i] + " ");
        }
        System.out.printf("\n");
    }

    /**
     * unit testing
     * @param args
     */
    public static void main(String[] args) {
        Percolation p = new Percolation(4);
        int[] rows = new int[]{0, 0, 2, 1, 2, 3, 2, 2};
        int[] cols = new int[]{0, 1, 0, 1, 1, 3, 3, 2};
        for (int i = 0; i < rows.length; i++) {
            System.out.println("\nOpen (" + rows[i] + ", " + cols[i] + "):");
            p.open(rows[i], cols[i]);
            p.print();
            System.out.println("Percolates? " + p.percolates());
        }
    }
}
