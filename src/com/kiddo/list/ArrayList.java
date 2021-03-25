package com.kiddo.list;

/**
 * @author FriskKiddo
 */
@SuppressWarnings("ALL")
public class ArrayList<E> extends AbstractList<E> implements List<E>{

    private Object[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 动态扩容
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= size + 1) {
            return;
        }
        int newCapacity = oldCapacity << 1;
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < elements.length; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
        System.out.println(capacity + "扩容为" + newCapacity);
    }

    public ArrayList(int capacity) {
        capacity = (capacity < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : capacity;
        elements = new Object[capacity];
    }

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void add(E e, int index) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = e;
        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        E e = (E) elements[index];
        for (int i = index + 1; i < size; i++) {
            elements[i - 1] = elements[i];
        }
            elements[--size] = null;
        trim();
        return e;
    }

    /**
     * 动态缩容
     */
    private void trim() {
        int capacity = elements.length;
        if (size < (capacity >> 2) && capacity > DEFAULT_CAPACITY) {
            int newCapacity = capacity >> 1;
            Object[] newElements = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newElements[i] = elements[i];
            }
            elements = newElements;
        }
    }

    /**
     * 清空内存
     */
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public E set(E e, int index) {
        rangeCheck(index);
        E oldElem = get(index);
        elements[index] = e;
        return oldElem;
    }

    @Override
    public E get(int index) {
        rangeCheck(index);
        return (E) elements[index];
    }

    @Override
    public int indexOf(E e) {
        if (e == null) {
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (elements[i].equals(e)) {
                    return i;
                }
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size = ").append(size).append(",[");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private boolean valEquals(E e1, E e2) {
        return e1 == null ? e2 == null : e1.equals(e2);
    }
}