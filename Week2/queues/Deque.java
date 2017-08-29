/******************************************************************************
 *  Compilation:  javac Deque.java
 *  Execution:    java Deque
 *
 *  Deque module API
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private int n; // size of deque
    private Node first; // first item
    private Node last; // last item
    
    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    // construct an empty deque
    public Deque() {
        n = 0;
        first = null;
        last = null;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }
    
    // return the number of items on the deque
    public int size() {
        return n;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item can't be null.");
        }
        
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (isEmpty()) {
            last = first;
        }
        else {
            oldfirst.prev = first;
        }
        ++n;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item can't be null.");
        }
        
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (isEmpty()) {
            first = last;
        }
        else {
            oldlast.next = last;
        }
        ++n;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("remove item from an empty queue");
        }
        
        Item item = first.item;
        first = first.next;
        --n;
        if (isEmpty()) {
            last = null;
        }
        else {
            first.prev = null;
        }
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("remove item from an empty queue");
        }
        
        Item item = last.item;
        last = last.prev;
        --n;
        if (isEmpty()) {
            first = null;
        }
        else {            
            last.next = null;
        }
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    // unit testing (optional)
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<Integer>();
        int n = 5;
        for (int i = 1; i <= n; ++i) {
            dq.addFirst(i);
        }
        
        for (int i = 1; i <= n; ++i) {
            StdOut.println(dq.removeLast());
        }
        
        for (int i = 1; i <= n; ++i) {
            dq.addFirst(i);
        }
        
        Iterator<Integer> i = dq.iterator();
        while (i.hasNext()) {
            StdOut.println(i.next()); 
        }
    }
}
