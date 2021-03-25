package com.kiddo.heap;

/**
 * @author FriskKiddo
 */
public interface Heap<E> {
    int size();

    boolean isEmpty();

    void clear();

    void add(E element);

    E remove();

    E get();

    E replace(E element);

}
