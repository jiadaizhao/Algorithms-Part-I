/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints input.txt
 *  Dependencies: Point.java, LineSegment.java
 *  
 *  FastCollinearPoints module API
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument to the constructor is null");
        }
        
        Point[] points = new Point[p.length];
        lineSegments = new ArrayList<LineSegment>();
        for (int i = 0; i < p.length; ++i) {
            if (p[i] == null) {
                throw new IllegalArgumentException("Null point in the array");
            }
            points[i] = p[i];
        }
        
        Arrays.sort(points);
        
        for (int i = 1; i < points.length; ++i) {
            if (points[i].compareTo(points[i - 1]) == 0) {
                throw new IllegalArgumentException("Contains a repeated point.");
            }
        }
        
        for (int i = 0; i < points.length - 3; ++i) {
            double[] slopesBefore = new double[i];
            for (int j = 0; j < i; ++j) {
                slopesBefore[j] = points[i].slopeTo(points[j]);
            }
            Point[] pointsAfter = new Point[points.length - i - 1];
            for (int j = 0; j < pointsAfter.length; ++j) {
                pointsAfter[j] = points[i + j + 1];
            }
            
            Arrays.sort(slopesBefore);
            Arrays.sort(pointsAfter, points[i].slopeOrder());
            
            int count = 1;
            double lastSlope = points[i].slopeTo(pointsAfter[0]);
            for (int j = 1; j < pointsAfter.length; ++j) {
                double slope = points[i].slopeTo(pointsAfter[j]);
                if (slope != lastSlope) {
                    if (count >= 3 && !duplicate(slopesBefore, lastSlope)) {
                        lineSegments.add(new LineSegment(points[i], pointsAfter[j - 1]));
                    }
                    count = 1;
                    lastSlope = slope;                    
                }
                else {
                    ++count;
                }
            }
            
            if (count >= 3 && !duplicate(slopesBefore, lastSlope)) {
                lineSegments.add(new LineSegment(points[i], pointsAfter[pointsAfter.length - 1]));
            }
        }
    }
    
    // Binary serach target slope
    private boolean duplicate(double[] slopesBefore, double target) {
        int low = 0;
        int high = slopesBefore.length - 1;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (slopesBefore[mid] > target) {
                high = mid - 1;
            }
            else if (slopesBefore[mid] < target) {
                low = mid + 1;
            }
            else {
                return true;
            }
        }
        return false;
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }
    
    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }
    
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
