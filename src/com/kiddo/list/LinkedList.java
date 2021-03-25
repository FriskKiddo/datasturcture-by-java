package com.kiddo.list;

/**
 * @author FriskKiddo
 */
public class LinkedList<E> extends AbstractList<E> {

    private Node<E> first;
    private Node<E> last;

    private static class Node<E> {
        E element;
        Node<E> prev;
        Node<E> next;

        public Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

    }

    private Node<E> node(int index) {
        rangeCheck(index);

        Node<E> node;
        if (index < (size >> 1)) {
            node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = last;
            for (int i = size - 1; i > index; i++) {
                node = node.prev;
            }
        }
        return node;
    }

    @Override
    public void add(E e, int index) {
        rangeCheckForAdd(index);

        if (index == size) {
            //index == size
            Node<E> oldLast = last;
            last = new Node<>(e, oldLast, null);
            if (oldLast == null) {
                //size == 0
                first = last;
            } else {
                oldLast.next = last;
            }
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> node = new Node<>(e, prev, next);
            next.prev = node;
            if (prev == null) {
                //index = 0
                first = node;
            } else {
                prev.next = node;
            }
        }
        size++;
    }

    @Override
    public int indexOf(E e) {
        Node<E> node = first;
        if (e == null) {
            for (int i = 0; i < size; i++) {
                if (node.element == null) {
                    return i;
                } else {
                    node = node.next;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (node.element == e) {
                    return i;
                } else {
                    node = node.next;
                }
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    @Override
    public E remove(int index) {
        Node<E> node = node(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;
        if (prev == null) {
            //index == 0
            first = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            //index == size - 1;
            last = prev;
        } else {
            next.prev = prev;
        }

        size--;
        return node.element;
    }

    @Override
    public void clear() {
        //没有用到迭代器
        first = null;
        last = null;
        size = 0;
    }

    @Override
    public E get(int index) {
        return node(index).element;
    }

    @Override
    public E set(E e, int index) {
        rangeCheck(index);

        Node<E> node = node(index);
        E old = node.element;
        node.element = e;
        return old;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("size: " + size + ", [");
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                builder.append(", ");
            }
            builder.append(node.element);
            node = node.next;
        }
        builder.append("]");
        return builder.toString();
    }

}
