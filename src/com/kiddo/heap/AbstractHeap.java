package com.kiddo.heap;

import java.util.Comparator;

/**
 * @author FriskKiddo
 */
public abstract class AbstractHeap<E> implements Heap<E> {

    protected int size;
    protected Comparator<E> comparator;

    public AbstractHeap() {
        this(null);
    }

    public AbstractHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 修改比较策略可以实现大顶堆和小顶堆
     */
    protected int compare(E e1, E e2) {
        return comparator == null ? ((Comparable<E>) e1).compareTo(e2) : comparator.compare(e1, e2);
    }

}
