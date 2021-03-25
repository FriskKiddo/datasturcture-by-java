package com.kiddo.list;

/**
 * @author FriskKiddo
 */
public interface List<E> {

    int ELEMENT_NOT_FOUND = -1;

    int size();

    boolean isEmpty();

    boolean contains(E e);

    int indexOf(E e);

    void add(E e);

    void add(E e, int index);

    E remove(int index);

    void clear();

    E set(E e, int index);

    E get(int index);
}
