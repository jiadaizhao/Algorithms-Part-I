/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation
 *
 *  Percolation module API
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {    
    private int width;
    private int openNumber;
    private boolean percolated;
    private WeightedQuickUnionUF wquuf;
    private boolean[] openUnit;    
    private boolean[] connectedTop;
    private boolean[] connectedBottom;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be larger than 0");
        }
        
        width = n;
        openNumber = 0;
        percolated = false;
        wquuf = new WeightedQuickUnionUF(n * n);
        openUnit = new boolean[n * n];
        connectedTop = new boolean[n * n];
        connectedBottom = new boolean[n * n];
        
        for (int i = 0; i < n * n; ++i) {
            openUnit[i] = false;
            connectedTop[i] = false;
            connectedBottom[i] = false;
        }
    }
    
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        
        ++openNumber;
        int index = (row - 1) * width + col - 1;
        openUnit[index] = true;
        boolean top = false, bottom = false;
        if (row == 1) {
            top = true;
        }
        
        if (row == width) {
            bottom = true;
        }
        
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        
        for (int i = 0; i < 4; ++i) {
            int nr = row + dx[i];
            int nc = col + dy[i];
            if (nr > 0 && nr <= width && nc > 0 && nc <= width && isOpen(nr, nc)) {
                int p = wquuf.find(index);
                int np = wquuf.find((nr - 1) * width + nc - 1);
                if (connectedTop[p] || connectedTop[np]) {
                    top = true;
                }
                
                if (connectedBottom[p] || connectedBottom[np]) {
                    bottom = true;
                }
                wquuf.union(p, np);
            }
        }
        
        int p = wquuf.find(index);
        if (top) {
            connectedTop[p] = true;
        }
        
        if (bottom) {
            connectedBottom[p] = true;
        }
        
        if (connectedTop[p] && connectedBottom[p]) {
            percolated = true;
        }
    }
    
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > width) {
            throw new IllegalArgumentException("row index out of bounds");
        }
        
        if (col <= 0 || col > width) {
            throw new IllegalArgumentException("col index out of bounds");
        } 
        return openUnit[(row - 1) * width + col - 1];
    }
    
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && 
            connectedTop[wquuf.find((row - 1) * width + col - 1)];
    }
    
    // number of open sites
    public int numberOfOpenSites() {
        return openNumber;
    }
    
    // does the system percolate?
    public boolean percolates() {
        return percolated;
    }
    
    // test client (optional)
    public static void main(String[] args) {
    }
}
