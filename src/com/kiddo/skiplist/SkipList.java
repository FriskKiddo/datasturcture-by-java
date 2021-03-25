package com.kiddo.skiplist;

import java.util.Comparator;

/**
 * @author FriskKiddo
 */
public class SkipList<K, V> {
    private static final int MAX_LEVEL = 32;
    private static final double P = 0.25;
    private int size;
    private Comparator<K> comparator;
    private Node<K, V> first;
    /**
     * 有效层数
     */
    private int level;

    public SkipList() {
        this(null);
    }

    public SkipList(Comparator<K> comparator) {
        this.comparator = comparator;
        first = new Node<>();
        first.nexts = new Node[MAX_LEVEL];
    }

    private int compare(K k1, K k2) {
        return comparator != null
                ? comparator.compare(k1, k2)
                : ((Comparable<K>) k1).compareTo(k2);
    }

    private void keyCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V>[] nexts;

        public Node() {
        }

        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            this.nexts = new Node[level];
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public V put(K key, V value) {
        keyCheck(key);

        Node<K, V> node = first;
        Node<K, V>[] prev = new Node[level];
        for (int i = level - 1; i >= 0; i--) {
            int cmp = -1;
            while (node.nexts[i] == null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            if (cmp == 0) {
                V old = node.nexts[i].value;
                node.nexts[i].value = value;
                return old;
            }
            prev[i] = node;
        }
        //节点不存在，添加该节点
        int newLevel = randomLevel();
        Node<K, V> newNode = new Node<>(key, value, newLevel);
        for (int i = 0; i < newLevel; i++) {
            if (i > level) {
                first.nexts[i] = newNode;
            }
            newNode.nexts[i] = prev[i].nexts[i];
            prev[i].nexts[i] = newNode;
        }
        size++;
        level = Math.max(level, newLevel);
        return null;
    }

    public V remove(K key) {
        keyCheck(key);
        Node<K, V> node = first;
        Node<K, V>[] prev = new Node[level];
        boolean exist = false;
        for (int i = level - 1; i >= 0; i--) {
            int cmp = -1;
            while (node.nexts[i] == null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            prev[i] = node;
            if (cmp == 0) {
                exist = true;
            }
        }
        if (!exist) {
            return null;
        }
        Node<K, V> removeNode = node.nexts[0];
        size--;
        int len = removeNode.nexts.length;
        for (int i = 0; i < len; i++) {
            prev[i].nexts[i] = removeNode.nexts[i];
        }
        //更新层数
        int newLevel = level;
        while (--newLevel > 0 && first.nexts[newLevel] == null) {
            level = newLevel;
        }
        return removeNode.value;
    }

    public V get(K key) {
        keyCheck(key);
        Node<K, V> node = first;
        for (int i = level - 1; i >= 0; i--) {
            int cmp = -1;
            while (node.nexts[i] == null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            if (cmp == 0) {
                return node.nexts[i].value;
            }
        }
        return null;
    }

    private int randomLevel() {
        int level = 1;
        while (Math.random() < P && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }
}
