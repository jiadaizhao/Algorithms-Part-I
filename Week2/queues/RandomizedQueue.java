/******************************************************************************
 *  Compilation:  javac RandomizedQueue.java
 *  Execution:    java RandomizedQueue
 *
 *  RandomizedQueue module API
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a; // array of items
    private int n; // number of elements on queue
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        n = 0;
    }
    
    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
    
    // return the number of items on the queue
    public int size() {
        return n;
    }
    
    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= n;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;

       // alternative implementation
       // a = java.util.Arrays.copyOf(a, capacity);
    } 
    
    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item can't be null.");
        }
        
        if (n == a.length) {
            resize(2*a.length);    // double size of array if necessary
        }
        a[n++] = item;             // add item
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("remove item from an empty queue");
        }
        
        int i = StdRandom.uniform(n);
        Item item = a[i];
        a[i] = a[n - 1];
        a[n - 1] = item;
        
        a[n - 1] = null;           // to avoid loitering
        --n;
        // shrink size of array if necessary
        if (n > 0 && n == a.length/4) {
            resize(a.length/2);
        }
        return item;     
    }
    
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("sample item from an empty queue");
        }
        
        int i = StdRandom.uniform(n);
        return a[i];
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;
        private final int[] indices;

        public RandomizedQueueIterator() {
            i = n - 1;
            indices = StdRandom.permutation(n);           
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return a[indices[i--]];
        }
    }    
    
    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        int n = 5;
        for (int i = 1; i <= n; ++i) {
            rq.enqueue(i);
        }
        
        for (int i = 1; i <= n; ++i) {
            StdOut.println(rq.dequeue());
        }
        
        for (int i = 1; i <= n; ++i) {
            rq.enqueue(i);
        }
        
        Iterator<Integer> i = rq.iterator();
        while (i.hasNext()) {
            StdOut.println(i.next()); 
        }
    }
}
