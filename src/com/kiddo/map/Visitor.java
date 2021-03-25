package com.kiddo.map;

public abstract class Visitor<K, V> {
    boolean stop;

    public abstract boolean visit(K key, V value);
}
