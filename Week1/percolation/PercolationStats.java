/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java Percolation n T
 *  Dependencies: Percolation.java
 *
 *  Percolation Monte Carlo simulation stats
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private int t;
    private double meanThreshold;
    private double stdThreshold;
    
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be larger than 0");
        }
        
        if (trials <= 0) {
            throw new IllegalArgumentException("trials must be larger than 0");
        }
        
        t = trials;
        double[] threshold = new double[t];
        
        for (int i = 0; i < trials; ++i) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                perc.open(row, col);
            }
            threshold[i] = perc.numberOfOpenSites() * 1.0 / (n * n);
        }
        
        meanThreshold = StdStats.mean(threshold);
        if (t == 1) {
            stdThreshold = Double.NaN;
        }
        else {
            stdThreshold = StdStats.stddev(threshold);
        }        
    }
    
    // sample mean of percolation threshold
    public double mean() {
        return meanThreshold;
    }
    
    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdThreshold;
    }
    
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return meanThreshold - 1.96 * stdThreshold / Math.sqrt(t);
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return meanThreshold + 1.96 * stdThreshold / Math.sqrt(t);
    }

    // test client
    public static void main(String[] args) {
        PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.printf("mean                    = %f\n", percStats.mean());
        StdOut.printf("stddev                  = %f\n", percStats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", percStats.confidenceLo(), percStats.confidenceHi());
    }
}
