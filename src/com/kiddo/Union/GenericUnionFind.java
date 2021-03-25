package com.kiddo.Union;

import com.kiddo.map.HashMap;
import com.kiddo.map.Map;

import java.util.Objects;

public class GenericUnionFind<V> {

    private Map<V, Node<V>> nodes = new HashMap<>();

    private static class Node<V> {
        V value;
        Node<V> parent = this;
        int rank = 1;

        public Node(V value) {
            this.value = value;
        }
    }

    public void makeSet(V v) {
        if (nodes.containsKey(v)) {
            return;
        }
        nodes.put(v, new Node<>(v));
    }

    public V find(V v) {
        Node<V> node = findNode(v);
        return node == null ? null : node.value;
    }

    public void union(V v1, V v2) {
        Node<V> p1 = findNode(v1);
        Node<V> p2 = findNode(v2);
        if (Objects.equals(p1.value, p2.value)) {
            return;
        }
        if (p1.rank > p2.rank) {
            p2.parent = p1;
        } else if (p1.rank < p2.rank) {
            p1.parent = p2;
        } else {
            p1.parent = p2;
            p2.rank++;
        }
    }

    public boolean isSame(V v1, V v2) {
        return Objects.equals(find(v1), find(v2));
    }

    /**
     * 查询v的根节点
     * @param v
     * @return
     */
    private Node<V> findNode(V v) {
        Node<V> node = nodes.get(v);
        if (node == null) {
            return null;
        }
        while(!Objects.equals(node.value, node.parent.value)){
            node.parent = node.parent.parent;
            node = node.parent;
        }
        return node;
    }

}
