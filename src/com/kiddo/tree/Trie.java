package com.kiddo.tree;

import java.util.HashMap;

/**
 * @author FriskKiddo
 */
@SuppressWarnings("all")
public class Trie<V> {

    private int size;
    private Node<V> root;

    private static class Node<V> {

        Character character;
        Node<V> parent;
        V value;
        boolean word = false; //是否是一个单词的结尾
        HashMap<Character, Node<V>> children = new HashMap<>();

        public Node(Node<V> parent) {
            this.parent = parent;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public void clear() {
        size = 0;
        root = null;
    }

    public boolean contains(String key) {
        Node<V> node = node(key);
        return node != null && node.word;
    }

    public V get(String key) {
        Node<V> node = node(key);
        return node != null && node.word ? node.value : null;
    }

    public V add(String key, V value) {
        //根节点判断
        if (root == null) {
            root = new Node<>(null);    //根
        }
        keyCheck(key);
        Node<V> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);
            Node<V> childNode = node.children.get(c);
            if (childNode == null) {
                childNode = new Node<>(node);    //child节点
                childNode.character = c;
                node.children.put(c, childNode);
            }
            node = childNode;
        }
        if (node.word) {
            //单词存在
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        } else {
            //新增一个单词
            node.word = true;
            node.value = value;
            size++;
            return null;
        }
    }

    public boolean startWith(String prefix) {
        return node(prefix) != null;
    }

    public V remove(String key) {
        //找到最后一个节点
        Node<V> node = node(key);
        V oldValue = node.value;

        //不是单词结尾不需要删除
        if (node == null || !node.word) {
            return null;
        }
        size--;
        //如果还有子节点
        if (!node.children.isEmpty()) {
            node.word = false;
            node.value = null;
        } else {
            //向上删除
            Node<V> parent;
            while ((parent = node.parent) != null) {
                parent.children.remove(node.character);
                if (parent.word ||!parent.children.isEmpty()) {
                    break;
                }
                node = parent;
            }
        }
        return oldValue;
    }

    private Node<V> node(String key) {
        keyCheck(key);

        Node<V> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            if (node == null) {
                return null;
            }
            char c = key.charAt(i);
            node = node.children.get(c);
        }
        return node!=null && node.word ? node : null; //node可能为null
    }

    private void keyCheck(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key must not be empty");
        }
    }

}
