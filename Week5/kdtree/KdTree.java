/******************************************************************************
 *  Compilation:  javac KdTree.java
 *  Execution:    java KdTree
 *  Dependencies:
 *  
 *  KdTree module API
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int num;
    private final double xmin, ymin, xmax, ymax;
    private static class Node {
        private final Point2D p;   // the point
        private final RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb;           // the left/bottom subtree
        private Node rt;           // the right/top subtree
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            lb = null;
            rt = null;
        }
    }    
    
    // construct an empty set of points 
    public KdTree() {
        root = null;
        num = 0;
        xmin = 0;
        ymin = 0;
        xmax = 1;
        ymax = 1;
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return num == 0;
    }
    
    // number of points in the set 
    public int size() {
        return num;
    }
    
    private int compareTo(Point2D p1, Point2D p2, boolean vertical) {
        if (p1.equals(p2)) {
            return 0;
        }
        
        if (vertical) {
            if (p1.x() < p2.x()) {
                return -1;
            }
            else {
                return 1;
            }
        }
        else {
            if (p1.y() < p2.y()) {
                return -1;
            }
            else {
                return 1;
            }
        }
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }
        
        if (root == null) {
            root = new Node(p, new RectHV(xmin, ymin, xmax, ymax));
            ++num;
            return;
        }
        
        boolean vertical = true;
        double x1 = xmin, y1 = ymin, x2 = xmax, y2 = ymax;
        Node prev = null;
        Node curr = root;
        int cmp = 0;
        while (curr != null) {
            prev = curr;
            cmp = compareTo(p, curr.p, vertical);
            if (cmp < 0) {
                if (vertical) {
                    x2 = curr.p.x();
                }
                else {
                    y2 = curr.p.y();
                }
                curr = curr.lb;
            }
            else if (cmp > 0) {
                if (vertical) {
                    x1 = curr.p.x();
                }
                else {
                    y1 = curr.p.y();
                }
                curr = curr.rt;
            }
            else {
                return;
            }
            
            vertical = !vertical;
        }
        
        ++num;
        Node node = new Node(p, new RectHV(x1, y1, x2, y2));
        if (cmp < 0) {
            prev.lb = node;
        }
        else {
            prev.rt = node;
        }
    }
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }
        
        boolean vertical = true;
        Node curr = root;
        int cmp = 0;
        while (curr != null) {
            cmp = compareTo(p, curr.p, vertical);
            if (cmp < 0) {
                curr = curr.lb;
            }
            else if (cmp > 0) {
                curr = curr.rt;
            }
            else {
                return true;
            }
            
            vertical = !vertical;
        }
        
        return false;
    }
   
    private void draw(Node node, boolean vertical) {
        if (node == null) {
            return;
        }
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        
        draw(node.lb, !vertical);
        draw(node.rt, !vertical);        
    }
    
    // draw all points to standard draw 
    public void draw() {
        draw(root, true);
    }
    
    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Rect is null");
        }
        
        Queue<Point2D> result = new Queue<Point2D>();
        if (root == null) {
            return result;
        }
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            if (rect.contains(node.p)) {
                result.enqueue(node.p);
            }
            
            if (node.lb != null && rect.intersects(node.lb.rect)) {
                queue.enqueue(node.lb);
            }
            
            if (node.rt != null && rect.intersects(node.rt.rect)) {
                queue.enqueue(node.rt);
            }
        }
        
        return result;
    }
    
    private Point2D nearest(Node node, Point2D minPoint, Point2D p, boolean vertical) {
        if (node == null) {
            return minPoint;
        }
        
        if (p.distanceSquaredTo(node.p) < p.distanceSquaredTo(minPoint)) {
            minPoint = node.p;
        }
        
        int cmp = compareTo(p, node.p, vertical);
        if (cmp < 0) {
            minPoint = nearest(node.lb, minPoint, p, !vertical);
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < p.distanceSquaredTo(minPoint)) {
                minPoint = nearest(node.rt, minPoint, p, !vertical);
            }
        }
        else if (cmp > 0) {
            minPoint = nearest(node.rt, minPoint, p, !vertical);
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < p.distanceSquaredTo(minPoint)) {
                minPoint = nearest(node.lb, minPoint, p, !vertical);
            }
        }
        
        return minPoint;
    }
    
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }
        
        if (root == null) {
            return null;
        }
        
        return nearest(root, root.p, p, true);
    }

    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.4, 0.7));
        ps.insert(new Point2D(0.9, 0.6));
        ps.draw();
        
        StdOut.println(ps.nearest(new Point2D(0.2, 0.2)));
        for (Point2D p : ps.range(new RectHV(0, 0.5, 1, 1))) {
            StdOut.println(p);
        }
    }
}
