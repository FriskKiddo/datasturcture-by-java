package com.kiddo.list;

import com.sun.media.sound.SF2InstrumentRegion;

import java.util.Arrays;

/**
 * @author FriskKiddo
 * points : (X + front) % elements.length
 */
public class CircleQueue<E> {
    private int front;
    private int size;
    private Object[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 真实索引映射
     * @param index
     * @return
     */
    private int index(int index) {
        return (index + front) % elements.length;
    }

    public CircleQueue() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public CircleQueue(int capacity) {
        capacity = (capacity < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : capacity;
        elements = new Object[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void offer(E e) {
        ensureCapacity(size + 1);
        elements[index(size)] = e;
        size++;
    }

    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) {
            return;
        }
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[index(i)];
        }
        front = 0;
        elements = newElements;
        System.out.println(oldCapacity + " to " + newCapacity);
    }

    public E poll() {
        E e = (E) elements[front];
        elements[front] = null;
        front = index(1);
        size--;
        return e;
    }

    public E peek() {
        return (E) elements[front];
    }


    public void clear(){
        for (int i = 0; i < size; i++) {
            elements[index(i)] = null;
        }
        size = 0;
        front = 0;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("capacity=" + elements.length)
                .append(" size=" + size +" front="+front+ "\r\n")
                .append("[");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(elements[i]);
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
