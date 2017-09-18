/******************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    java PointSET
 *  Dependencies:
 *  
 *  PointSET module API
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {
    private final SET<Point2D> set;
    
    // construct an empty set of points 
    public PointSET() {
        set = new SET<Point2D>();
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    // number of points in the set 
    public int size() {
        return set.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        return set.contains(p);
    }
   
    // draw all points to standard draw 
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }
    
    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rect is null");
        }
        
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                queue.enqueue(p);
            }
        }
        
        return queue;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }
        
        Point2D result = null;
        double minDist = Double.MAX_VALUE;
        for (Point2D point : set) {
            double dist = point.distanceSquaredTo(p);
            if (dist < minDist) {
                minDist = dist;
                result = point;
            }
        }
        
        return result;
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0, 0));
        ps.insert(new Point2D(0, 1));
        ps.insert(new Point2D(1, 0));
        ps.insert(new Point2D(1, 1));
        ps.draw();
        
        StdOut.println(ps.nearest(new Point2D(0.2, 0.2)));
        for (Point2D p : ps.range(new RectHV(0, 0.5, 1, 1))) {
            StdOut.println(p);
        }
    }
}
