package com.kiddo.graph;

public interface WeightManager<E> {
    int compare(E w1, E w2);

    E add(E w1, E w2);

    E zero();
}
