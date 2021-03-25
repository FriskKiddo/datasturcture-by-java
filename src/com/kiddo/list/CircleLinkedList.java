package com.kiddo.list;

/**
 * @author FriskKiddo
 */
public class CircleLinkedList<E> extends AbstractList<E> implements List<E> {

    private Node<E> first;
    private Node<E> last;
    private Node<E> current;

    private static class Node<E> {
        E element;
        Node<E> prev;
        Node<E> next;

        public Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return prev.element + "_" + element + "_" + next.element;
        }
    }

    private Node<E> node(int index) {
        rangeCheck(index);

        Node<E> node;
        if (index < (size >> 1)) {
            node = first;
            while (index-- > 0) {
                node = node.next;
            }
        } else {
            node = last;
            while (index-- > 0) {
                node = node.prev;
            }
        }
        return node;
    }

    public void reset() {
        current = first;
    }

    public E next() {
        if (current == null) {
            return null;
        }
        E element = current.element;
        current = current.next;
        return element;
    }

    public E remove() {
        if (current == null) {
            return null;
        }
        Node<E> next =current.next;
        E element = remove(current);
        if (size == 0){
            current = null;
        }else {
            current = next;
        }
        return element;
    }

    private E remove(Node<E> node) {
        if (size == 1) {
            first = null;
            last = null;
        } else {
            Node<E> prev = node.prev;
            Node<E> next = node.next;
            if (node == first) {
                first = next;
            } else {
                prev.next = next;
            }
            if (node == last) {
                last = prev;
            } else {
                next.prev = prev;
            }
        }
        size--;
        return node.element;
    }

    @Override
    public void add(E e, int index) {
        rangeCheckForAdd(index);

        if (index == size) {
            //index == size
            Node<E> oldLast = last;
            last = new Node<>(e, oldLast, first);
            if (oldLast == null) {
                //size == 0
                first = last;
                first.next = first;
                first.prev = first;
            } else {
                oldLast.next = last;
                //first的prev指向最后一个
                first.prev = last;
            }
        } else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> node = new Node<>(e, prev, next);
            next.prev = node;
            prev.next = node;
            if (index == 0) {
                first = node;
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
        rangeCheck(index);
        return remove(node(index));
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
            builder.append(node.toString());
            node = node.next;
        }
        builder.append("]");
        return builder.toString();
    }
}
