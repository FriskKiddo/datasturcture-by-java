package com.kiddo.map;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author FriskKiddo
 */
@SuppressWarnings("all")
public class TreeMap<K, V> implements Map<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private int size;
    private Node<K, V> root;
    private Comparator<K> comparator;

    private static class Node<K, V> {
        boolean color = RED;
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChild()) {
                return parent.left;
            }
            return null;
        }

    }

    public TreeMap() {
        this(null);
    }

    public TreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    private int compare(K k1, K k2) {
        if (comparator != null) {
            return comparator.compare(k1, k2);
        }
        return ((Comparable<K>) k1).compareTo(k2);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public V put(K key, V value) {
        keyNotNullCheck(key);
        //添加第一个节点
        if (root == null) {
            root = new Node<>(key, value, null);
            size++;
            black(root);
            return null;
        }

        //添加的不是第一个结点
        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        do {
            parent = node;
            cmp = compare(key, node.key);
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        } while (node != null);
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
        afterPut(newNode);
        return null;
    }

    private void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;
        if (parent == null) {
            //添加的是根节点
            black(node);
        }
        if (isBlack(parent)) {
            //若父节点是黑色
            return;
        }
        //判断uncle节点
        Node<K, V> uncle = node.parent.sibling();
        Node<K, V> grand = parent.parent;
        if (isRed(uncle)) {
            //uncle节点是红色
            black(parent);
            black(uncle);
            //祖父节点向上合并
            afterPut(red(grand));
        } else {
            //uncle节点不是红色
            if (parent.isLeftChild()) {
                red(grand);
                if (node.isLeftChild()) {
                    //LL
                    black(parent);
                } else {
                    //LR
                    black(node);
                    rotateLeft(parent);
                }
                rotateRight(grand);
            } else {
                red(grand);
                if (node.isLeftChild()) {
                    //RL
                    black(node);
                    rotateRight(parent);
                } else {
                    //RR
                    black(parent);
                }
                rotateLeft(grand);
            }
        }
    }

    protected void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
        grand.right = child;
        parent.left = grand;
        afterRotate(grand, parent, child);
    }

    protected void rotateRight(Node<K, V> grand) {
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;
        grand.left = child;
        parent.right = grand;
        afterRotate(grand, parent, child);
    }

    protected void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
        //从上到下更新parent、grand、child的parent
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else {
            root = parent;
        }
        grand.parent = parent;
        if (child != null) {
            child.parent = grand;
        }
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node == null ? null : node.value;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (root == null) {
            return false;
        }
        Queue<Node<K, V>> queue = new LinkedList<>();
        queue.offer(root);
        Node<K, V> node = null;
        while (!queue.isEmpty()) {
            node = queue.poll();
            if (valEquals(value, node.value)) {
                return true;
            }
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) {
            return;
        }
        traversal(root, visitor);

    }

    private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
        if (node == null || visitor.stop) {
            return;
        }
        traversal(node.left, visitor);
        if (visitor.stop) {
            return;
        }
        visitor.visit(node.key, node.value);
        traversal(node.right, visitor);
    }


    private void keyNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
    }

    private boolean valEquals(V v1, V v2) {
        return v1 == null ? v2 == null : v1.equals(v2);
    }

    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) {
            return node;
        }
        node.color = color;
        return node;
    }

    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    private Node<K, V> node(K key) {
        Node<K, V> node = root;
        while (node != null) {
            int cmp = compare(key, node.key);
            if (cmp == 0) {
                return node;
            }
            if (cmp > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return node;
    }

    private V remove(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        size--;

        V oldValue = node.value;
        //度为2
        if (node.hasTwoChildren()) {
            Node<K, V> pred = predecessor(node);
            node.key = pred.key;
            node.value = pred.value;
            node = pred;
        }

        //统一化，删除node节点（node的度必然为1或者0）
        Node<K, V> replacement = node.left != null ? node.left : node.right;

        if (replacement != null) {
            //度为1
            replacement.parent = node.parent;
            if (node.parent == null) {
                //根节点
                root = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else {
                node.parent.right = replacement;
            }
            //删除节点后的处理
            afterRemove(replacement);
        } else if (node.parent == null) { //度为0
            //根节点
            root = null;
        } else {
            //叶子节点且非根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
            afterRemove(node);
        }
        return oldValue;
    }

    private Node<K, V> predecessor(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        Node<K, V> p = node.left;
        //前驱节点在左子树中
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }
        //前驱节点在父节点中
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }

        //node.parent == null || node == node.parent.right
        return node.parent;
    }

    private Node<K, V> successor(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        //后驱节点在右子树
        Node<K, V> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        //后驱节点在父节点中
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    private void afterRemove(Node<K, V> node) {

        if (isRed(node)) {
            //删除节点是红色或者用以取代删除的节点是红色
            black(node);
            return;
        } else {
            Node<K, V> parent = node.parent;

            if (parent == null) {
                return;
            }
            //删除黑色叶子节点【下溢】
            //判断被删除的node是左是右
            boolean left = parent.left == null || node.isLeftChild();
            Node<K, V> sibling = left ? parent.right : parent.left;

            if (left) {
                //被删除的节点在左边，兄弟节点在右边
                if (isRed(sibling)) {
                    //兄弟节点是红色，转换成黑色情况
                    black(sibling);
                    red(parent);
                    rotateLeft(parent);
                    //替换兄弟
                    sibling = parent.right;
                }
                //兄弟节点必然是黑色
                if (isBlack(sibling.right) && isBlack(sibling.left)) {
                    //兄弟节点没有红色子节点，父节点向下合并
                    boolean parentBlack = isBlack(parent);
                    black(parent);
                    red(sibling);
                    if (parentBlack) {
                        afterRemove(parent);
                    }
                } else {
                    //兄弟节点至少右一个红色子节点，向兄弟节点借元素
                    if (isBlack(sibling.right)) {
                        //兄弟右边为黑，兄弟先旋转
                        rotateRight(sibling);
                        sibling = parent.right;
                    }
                    color(sibling, colorOf(parent));
                    black(sibling.right);
                    black(parent);
                    rotateLeft(parent);
                }
            } else {
                //被删除的节点在右边，兄弟节点在左边
                if (isRed(sibling)) {
                    //兄弟节点是红色，转换成黑色情况
                    black(sibling);
                    red(parent);
                    rotateRight(parent);
                    //替换兄弟
                    sibling = parent.left;
                }
                //兄弟节点必然是黑色
                if (isBlack(sibling.left) && isBlack(sibling.right)) {
                    //兄弟节点没有红色子节点，父节点向下合并
                    boolean parentBlack = isBlack(parent);
                    black(parent);
                    red(sibling);
                    if (parentBlack) {
                        afterRemove(parent);
                    }
                } else {
                    //兄弟节点至少右一个红色子节点，向兄弟节点借元素
                    if (isBlack(sibling.left)) {
                        //兄弟左边为黑，兄弟先旋转
                        rotateLeft(sibling);
                        sibling = parent.left;
                    }
                    color(sibling, colorOf(parent));
                    black(sibling.left);
                    black(parent);
                    rotateRight(parent);
                }
            }
        }

    }
}
