package com.kiddo.list;

import com.kiddo.heap.MaxHeap;

import java.util.Comparator;

/**
 * @author FriskKiddo
 */
public class PriorityQueue<E> {

    private MaxHeap<E> heap;

    public PriorityQueue() {
        this(null);
    }

    public PriorityQueue(Comparator<E> comparator) {
        heap = new MaxHeap<>(comparator);
    }

    public int size() {
       return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    public void enQueue(E element) {
        heap.add(element);
    }

    public E deQueue() {
        return heap.remove();
    }

    public E front() {
        return heap.get();
    }

}
