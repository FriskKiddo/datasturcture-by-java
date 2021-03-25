package com.kiddo.list;

/**
 * @author FriskKiddo
 */
public abstract class AbstractList<E> implements List<E>{

    protected int size;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E e) {
        return indexOf(e) != ELEMENT_NOT_FOUND;
    }

    @Override
    public void add(E e) {
        add(e, size);
    }

    protected void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            outOfBounds(index);
        }
    }

    protected void rangeCheckForAdd(int index) {
        if (index < 0 || index > size) {
            outOfBounds(index);
        }
    }

    protected void outOfBounds(int index) {
        throw new IndexOutOfBoundsException("size : " + size + ", index :" + index);
    }

}
