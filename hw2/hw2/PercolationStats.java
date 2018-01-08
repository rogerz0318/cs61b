package hw2;                       

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private Percolation p;
    private int n;
    private int t;
    private double[] results;

    /**
     * perform T independent experiments on an N-by-N grid
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        n = N;
        t = T;
        results = new double[t];
    }

    public void run() {
        for (int i = 0; i < t; i++) {
            runExp(i);
        }
    }

    private void runExp(int iExp) {
        p = new Percolation(n);
        while (!p.percolates()) {
            /*
            int nBlocked = n * n - p.numberOfOpenSites();
            int toOpen = StdRandom.uniform(nBlocked); // random int between 0 and n-1
            int iBlocked = 0; // keep track of index of blocked site
            for (int i = toOpen; i < n * n; i++) {
                // instead of starting from zero, start from toOpen
                int row = i / n;
                int col = i % n;
                if (!p.isOpen(row, col)) {
                    // blocked site, is index equal to toOpen?
                    if (iBlocked == toOpen) {
                        // if so, open it
                        p.open(row, col);
                        break;
                    } else {
                        // otherwise, increment the blocked site index and loop on
                        iBlocked++;
                    }
                }
            }
            */
            // another version as suggested in HW FAQ
            // this version is much efficient, it does not need to iterate through array
            while (true) {
                int toOpen = StdRandom.uniform(n * n); // random int between 0 and n-1
                int row = toOpen / n;
                int col = toOpen % n;
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                    break;
                }
            }
        }
        // collect threshold results
        results[iExp] = ((double) p.numberOfOpenSites()) / (n * n);
    }

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * low endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(n);
    }

    /**
     * high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(n);
    }

    public static void main(String[] args) {
        int dimension = 400;
        int sampleSize = 50;
        PercolationStats stats = new PercolationStats(dimension, sampleSize);
        System.out.println("PercolationStats: Dimension = " + dimension + ", Sample Size = " + sampleSize);
        stats.run();
        System.out.println("Threshold Statistics:");
        System.out.println("Mean = " + stats.mean());
        System.out.println("Standard Deviation = " + stats.stddev());
        System.out.println("95% Confidence Interval = [" + stats.confidenceLow() + ", " + stats.confidenceHigh() + "]");
    }
}                       
