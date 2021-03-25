package com.kiddo.list;

import sun.awt.image.ImageWatched;

/**
 * @author FriskKiddo
 */
public class Stack<E> {

    private LinkedList<E> list;

    public Stack() {
        list = new LinkedList<>();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void push(E e) {
        list.add(e);
    }

    public E pop() {
        return list.remove(list.size() - 1);
    }

    public E peek() {
        return list.get(list.size() - 1);
    }
}

