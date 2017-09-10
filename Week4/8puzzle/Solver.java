/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver input.txt
 *  Dependencies: Board.java
 *  
 *  Solver module API
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int move;
    private final Stack<Board> solutionStack = new Stack<Board>();
    private class Node implements Comparable<Node> {
        private final Board board;
        private final int move;
        private final int manhanttanDistance;
        private final int priority;
        private final Node prev;
        private final boolean isTwin;
        
        public Node(Board board, int move, Node node, boolean isTwin) {
            this.board = board;
            this.move = move;
            this.manhanttanDistance = board.manhattan();
            this.priority = this.manhanttanDistance + move;
            this.prev = node;
            this.isTwin = isTwin;
        }
        
        public int compareTo(Node node) {
            if (priority < node.priority) {
                return -1;
            }
            else if (priority > node.priority) {
                return 1;
            }
            else if (manhanttanDistance < node.manhanttanDistance) {
                return -1;
            }
            else if (manhanttanDistance > node.manhanttanDistance) {
                return 1;
            }
            else {
                return 0;
            }
        }
    } 
        
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board is null.");
        }
        
        MinPQ<Node> pq = new MinPQ<Node>();
        Node initialNode = new Node(initial, 0, null, false);
        Node initialNodeTwin = new Node(initial.twin(), 0, null, true);
        pq.insert(initialNode);
        pq.insert(initialNodeTwin);
        
        while (!pq.isEmpty()) {
            Node node = pq.delMin();
            if (node.board.isGoal()) {
                if (node.isTwin) {
                    this.move = -1;
                }
                else {
                    this.move = node.move;                    
                    saveSolution(node);
                }
                
                break;
            }
            
            for (Board neighbor : node.board.neighbors()) {
                if (node.prev == null || !node.prev.board.equals(neighbor)) {
                    Node neighborNode = new Node(neighbor, node.move + 1,
                                                 node, node.isTwin);
                    pq.insert(neighborNode);
                }
            }
        }
    }
    
    private void saveSolution(Node node) {
        while (node != null) {
            solutionStack.push(node.board);
            node = node.prev;
        }
    }
    
    // is the initial board solvable?
    public boolean isSolvable() {
        return move != -1;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return move;
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solutionStack;
        }
        else {
            return null;
        }
    }
    
    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
