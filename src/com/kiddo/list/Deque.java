package com.kiddo.list;

/**
 * @author FriskKiddo
 */
public class Deque<E> {

    private LinkedList<E> list = new LinkedList<>();

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void clear() {
        list.clear();
    }

    public void enQueueFront(E e) {
        list.add(e,0);
    }

    public void enQueueRear(E e) {
        list.add(e);
    }

    public E deQueueFront() {
        return list.remove(0);
    }

    public E deQueueRear() {
        return list.remove(list.size() - 1);
    }

    public E front() {
        return list.get(0);
    }

    public E Rear() {
        return list.get(list.size() - 1);
    }
}
