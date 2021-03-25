package com.kiddo.list;


/**
 * @author FriskKiddo
 */
public class Queue<E>{

    private LinkedList<E> list;

    public Queue() {
        list = new LinkedList<>();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }

    public void offer(E e) {
        list.add(e);
    }

    public E poll() {
        return list.remove(0);
    }

    public E peek() {
        return list.get(0);
    }
}
