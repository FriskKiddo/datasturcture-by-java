package com.kiddo.utils;

public abstract class Visitor<E> {
    public boolean stop;

    public abstract boolean visit(E element);
}
