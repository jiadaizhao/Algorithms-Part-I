/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java Board
 *  Dependencies:
 *  
 *  Board module API
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int n;
    private final int hammingDistance;
    private final int manhanttanDistance;
    private final int[][] tiles;
    private int blankx, blanky;
        
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        int hd = 0;
        int md = 0;
        
        tiles = new int[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                tiles[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    blankx = i;
                    blanky = j;
                }
                else if (blocks[i][j] != i * n + j + 1) {
                    ++hd;
                    md += Math.abs(i - (blocks[i][j] - 1) / n);
                    md += Math.abs(j - (blocks[i][j] - 1) % n);
                }
            }
        }
        
        hammingDistance = hd;
        manhanttanDistance = md;
    }
    
    // board dimension n
    public int dimension() {
        return n;
    }
    
    // number of blocks out of place
    public int hamming() {
        return hammingDistance;
    }
    
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhanttanDistance;
    }
    
    // is this board the goal board?
    public boolean isGoal() {
        return hammingDistance == 0;
    }
    
    private void swap(int[][] matrix, int x1, int y1, int x2, int y2) {
        int temp = matrix[x1][y1];
        matrix[x1][y1] = matrix[x2][y2];
        matrix[x2][y2] = temp;
    }
    
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {        
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (j < n - 1 && tiles[i][j] != 0 && tiles[i][j + 1] != 0) {
                    swap(tiles, i, j, i, j + 1);
                    Board result = new Board(tiles);
                    swap(tiles, i, j, i, j + 1);
                    return result;
                }
            }
        }
        
        throw new IllegalArgumentException();
    }
    
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (n != that.dimension()) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<Board>();
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        for (int i = 0; i < dx.length; ++i) {
            int x = blankx + dx[i];
            int y = blanky + dy[i];
            if (x >= 0 && x < n && y >= 0 && y < n) {
                swap(tiles, blankx, blanky, x, y);
                queue.enqueue(new Board(tiles));
                swap(tiles, blankx, blanky, x, y);              
            }            
        }
        
        return queue;
    }
    
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] blocks = {
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5},
        };
        
        Board b = new Board(blocks);
        StdOut.println(b.toString());
        StdOut.println("Dimension: " + b.dimension());
        StdOut.println("Hamming distance: " + b.hamming());
        StdOut.println("Manhattan distance: " + b.manhattan());
        
        Board t = b.twin();
        StdOut.println("Block twin: " + t.toString());
        StdOut.println(b.equals(t));
        
        for (Board it : b.neighbors()) {
            StdOut.println(it.toString());
            StdOut.println("Dimension: " + it.dimension());
            StdOut.println("Hamming distance: " + it.hamming());
            StdOut.println("Manhattan distance: " + it.manhattan());
        }
    }
}
