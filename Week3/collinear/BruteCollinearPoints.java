/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints input.txt
 *  Dependencies: Point.java, LineSegment.java
 *  
 *  BruteCollinearPoints module API
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] p) {
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
        
        for (int ip = 0; ip < points.length - 3; ++ip) {
            for (int iq = ip + 1; iq < points.length - 2; ++iq) {
                double slopePQ = points[ip].slopeTo(points[iq]);
                for (int ir = iq + 1; ir < points.length - 1; ++ir) {
                    double slopeQR = points[iq].slopeTo(points[ir]);
                    if (slopePQ != slopeQR) {
                        continue;
                    }
                    for (int is = ir + 1; is < points.length; ++is) {
                        double slopeRS = points[ir].slopeTo(points[is]);
                        if (slopeQR == slopeRS) {
                            lineSegments.add(new LineSegment(points[ip], points[is]));
                        }
                    }
                }
            }
        }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
