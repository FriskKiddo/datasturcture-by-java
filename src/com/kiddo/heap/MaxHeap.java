package com.kiddo.heap;

import com.kiddo.printer.BinaryTreeInfo;

import java.util.Collection;
import java.util.Comparator;

/**
 * @author FriskKiddo
 */
@SuppressWarnings("all")
public class MaxHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {

    private static final int DEFAULT_CAPACITY = 10;
    private E[] elements;

    public MaxHeap(Collection<E> collection) {
        this(collection, null);
    }

    public MaxHeap(E[] elements) {
        this(elements, null);
    }

    public MaxHeap(Comparator<E> comparator) {
        this.comparator = comparator;
        elements = (E[]) new Object[DEFAULT_CAPACITY];
    }

    public MaxHeap(E[] elements, Comparator<E> comparator) {
        super(comparator);
        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            //深拷贝
            int capacity = Math.max(elements.length, DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capacity];
            for (int i = 0; i < capacity; i++) {
                this.elements[i] = elements[i];
            }
            size = elements.length;
            heapify();
        }
    }

    public MaxHeap(Collection<E> elements, Comparator<E> comparator) {
        this.comparator = comparator;
        size = elements == null ? 0 : elements.size();
        if (size == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            int capacity = Math.max(size, DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capacity];
            int i = 0;
            for (E element : elements) {
                this.elements[i++] = element;
            }
            heapify();
        }
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public void add(E elememt) {
        elementNotNullCheck(elememt);
        ensureCapacity(size + 1);
        elements[size++] = elememt;
        siftUp(size - 1);
    }

    @Override
    public E remove() {
        emptyCheck();
        E root = elements[0];
        int lastIndex = --size;
        elements[0] = elements[lastIndex];
        elements[lastIndex] = null;
        siftDown(0);
        return root;
    }

    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    @Override
    public E replace(E element) {
        elementNotNullCheck(element);
        E root = null;
        if (size == 0) {
            elements[0] = element;
            size++;
        } else {
            root = elements[0];
            elements[0] = element;
            siftDown(0);
        }
        return null;
    }

    private void emptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("heap is empty");
        }
    }

    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= size + 1) {
            return;
        }
        int newCapacity = oldCapacity << 1;
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < elements.length; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }

    /**
     * 让index的元素上滤
     *
     * @param index
     */
    private void siftUp(int index) {
        E e = elements[index];
        while (index > 0) {
            int parentIndex = (index - 1) >> 1;
            E parent = elements[parentIndex];
            if (compare(e, parent) <= 0) {
                break;
            } else {
                //父元素存储在index
                elements[index] = parent;
                index = parentIndex;
            }
        }
        elements[index] = e;
    }


    /**
     * 让index的元素下滤
     *
     * @return
     */
    private void siftDown(int index) {
        int half = size >> 1; // 第一个叶子节点索引==非叶子节点数量
        E element = elements[index];
        while (index < half) {
            //index的节点情况右两种
            // 1.只有左子节点
            // 2. 同时有左右两个子节点

            //默认左节点
            int childIndex = (index << 1) + 1;

            //判断右子节点
            int rightIndex = childIndex + 1;
            if (rightIndex < size && compare(elements[rightIndex], elements[childIndex]) > 0) {
                childIndex = rightIndex;
            }
            E child = elements[childIndex];

            if (compare(element, child) >= 0) {
                break;
            }
            //将子节点存放到index位置
            elements[index] = child;
            //更新index
            index = childIndex;
        }
        elements[index] = element;
    }

    public void heapify() {
//        //自上而下的上滤
//        for (int i = 1; i < size; i++) {
//            siftUp(i);
//        }

        //自下而上的下滤（从非叶子节点开始）-----效率更高
        for (int i = (size >> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    @Override
    public Object root() {
        return 0;
    }

    @Override
    public Object left(Object node) {
        int left = ((int) node << 1) + 1;
        return left >= size ? null : left;
    }

    @Override
    public Object right(Object node) {
        int right = ((int) node << 1) + 2;
        return right >= size ? null : right;
    }

    @Override
    public Object string(Object node) {
        return elements[(int) node];
    }
}
