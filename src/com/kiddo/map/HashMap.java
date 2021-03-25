package com.kiddo.map;

import com.kiddo.printer.BinaryTreeInfo;
import com.kiddo.printer.BinaryTrees;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FriskKiddo
 */

@SuppressWarnings("all")
public class HashMap<K, V> implements Map<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private int size;
    private Node<K, V>[] table;
    private static int DEFAULT_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public HashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

//    private

    private static class Node<K, V> {

        int hash;
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
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);   //扰动计算
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

        @Override
        public String toString() {
            return "Node_" + key + "_" + value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if (size == 0) {
            return;
        }
        int len = table.length;
        for (int i = 0; i < len; i++) {
            table[i] = null;
        }
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        resize();
        int index = index(key);
        Node<K, V> root = table[index];
        if (root == null) {
            root = new Node<>(key, value, null);
            table[index] = root;
            size++;
            afterPut(root);
            return null;
        } else {
            //哈希冲突，添加新的节点到红黑树
            Node<K, V> parent = root;
            Node<K, V> node = root;
            int cmp = 0;
            K k1 = key;
            int h1 = hash(key);
            Node<K, V> result = null;
            //是否搜索过key
            boolean searched = false;
            do {
                parent = node;
                K k2 = node.key;
                int h2 = node.hash;
                if (h1 > h2) {
                    cmp = 1;
                } else if (h1 < h2) {
                    cmp = -1;
                } else if (Objects.equals(k1, k2)) {
                    cmp = 0;
                } else if (k1 != null && k2 != null
                        && k1 instanceof Comparable
                        && k1.getClass() == k2.getClass()
                        && ((Comparable) k1).compareTo(k2) != 0) {
                    //do nothing，
                } else if (searched) { //先判断是否已经扫描
                    //已扫描
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                } else {
                    // 未扫描
                    if ((node.left != null && (result = node(node.left, k1)) != null
                            || (node.right != null && (result = node(node.right, k1)) != null))) {
                        //存在这个key
                        node = result;
                        cmp = 0;
                    } else {
                        //不存在这个key，根据内存地址大小决定左右
                        cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                        searched = true;
                    }
                }
                if (cmp == 0) {
                    node.key = key;
                    V old = node.value;
                    node.value = value;
                    return old;
                } else {
                    node = cmp > 0 ? node.right : node.left;
                }
            } while (node != null);
            Node<K, V> newNode = new Node<>(key, value, parent);
            if (cmp > 0) {
                parent.right = newNode;
            } else {
                parent.left = newNode;
            }
            afterPut(newNode);
            size++;
            return null;
        }
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    @Override
    public boolean containsKey(K key) {
        Node<K, V> node = node(key);
        return node != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (size == 0) {
            return false;
        }
        int len = table.length;
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            if (table[i] == null) {
                continue;
            }
            queue.offer(table[i]);
            Node<K, V> node;
            while (!queue.isEmpty()) {
                node = queue.poll();
                if (Objects.equals(node.value, value)) {
                    return true;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null) {
            return;
        }
        Queue<Node<K, V>> queue = new LinkedList<>();
        int len = table.length;
        for (int i = 0; i < len; i++) {
            queue.offer(table[i]);
            Node<K, V> node;
            while (!queue.isEmpty()) {
                node = queue.poll();
                if (visitor.visit(node.key, node.value)) {
                    return;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
    }

    public void print() {
        if (size == 0) {
            return;
        }
        int len = table.length;
        for (int i = 0; i < len; i++) {
            final Node<K, V> root = table[i];
            if (root != null) {
                System.out.println("[index = " + i + "]");
            }
            BinaryTrees.println(new BinaryTreeInfo() {
                @Override
                public Object root() {
                    return root;
                }

                @Override
                public Object left(Object node) {
                    return ((Node<K, V>) node).left;
                }

                @Override
                public Object right(Object node) {
                    return ((Node<K, V>) node).right;
                }

                @Override
                public Object string(Object node) {
                    return node.toString();
                }
            });
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
        //从上到下更新parent、grand、child的parent和峰顶源
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else { //grand所在的红黑树
            table[index(grand)] = parent;
        }
        grand.parent = parent;
        if (child != null) {
            child.parent = grand;
        }
    }

    /**
     * 根据k生成对应索引
     *
     * @param key
     * @return
     */
    private int index(K key) {
        return hash(key) & (table.length - 1);
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }

    private int index(Node<K, V> node) {
        return node.hash & (table.length - 1);
    }

    private void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;
        if (parent == null) {
            //添加的是根节点
            black(node);
            return;
        }
        if (isBlack(parent)) {
            //若父节点是黑色
            return;
        }
        //判断uncle节点
        Node<K, V> uncle = parent.sibling();
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

    private void afterRemove(Node<K, V> node) {
        //
        //
        //


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

    /**
     * 比较哈希值
     *
     * @param k1
     * @param k2
     * @param h1 k1哈希值
     * @param h2 k2哈希值
     * @return
     */
//    private int compare(K k1, K k2, int h1, int h2) {
////        int result = h1 - h2;
////        if (result != 0) {
////            return result;
////        }
////        if (Objects.equals(k1, k2)) {
////            //哈希值相等，equlas相等
////            return 0;
////        }
////        //哈希值相等，equals不相等
////        //比较类名
////        if (k1 != null && k2 != null) {
////            String k1Class = k1.getClass().getName();
////            String k2Class = k2.getClass().getName();
////            result = k1Class.compareTo(k2Class);
////            if (result != 0) {
////                return result;
////            }
////            //同一种类型
////            if (k1 instanceof Comparable) {
////                //具备可比较性
////                return ((Comparable) k1).compareTo(k2);
////            }
////        }
////        //1. 同一类型，不是同一对象，哈希值一样，但不具备可比较性
////        //2. k1为null，k2不为null
////        //3. k2为null，k1不为null
////        return System.identityHashCode(k1) - System.identityHashCode(k2);
////    }

    /**
     * 通过key查找以node为根的红黑树中的对应节点
     *
     * @param node
     * @param k1
     * @return
     */
    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = hash(k1);
        Node<K, V> result = null;
        int cmp = 0;
        while (node != null) {
            K k2 = node.key;
            int h2 = node.hash;
            //比较哈希值，两者相减可能溢出
            if (h1 > h2) {
                node = node.right;
            } else if (h1 < h2) {
                node = node.left;
            } else if (Objects.equals(k1, k2)) {
                return node;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && ((cmp = ((Comparable) k1).compareTo(k2)) != 0)) {
                node = cmp > 0 ? node.right : node.left;
                //哈希值相等，equals不等，不具备可比较性或者具备可比较性但是compareTo方法相等
                //无法判断，采用逐个扫描（策略不唯一）
            } else if (node.right != null && (result = node(node.right, k1)) != null) {
                return result;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    private Node<K, V> node(K key) {
        Node<K, V> root = table[index(key)];
        return root == null ? null : node(root, key);
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

    private V remove(Node<K, V> node) {
        if (node == null) {
            return null;
        }
        size--;
        V old = node.value;

        if (node.hasTwoChildren()) {
            Node<K, V> next = successor(node);
            //替换key value hash
            node.value = next.value;
            node.key = next.key;
            node.hash = next.hash;
            node = next;
        }
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        int index = index(node);
        if (replacement != null) {
            replacement.parent = node.parent;
            if (node.parent == null) {
                table[index] = null;
            }
            if (node.isLeftChild()) {
                node.parent.left = replacement;
            } else if (node.isRightChild()) {
                node.parent.right = replacement;
            }
        } else {
            if (node.parent == null) {
                table[index] = null;
            }
            if (node.isLeftChild()) {
                node.parent.left = null;
            } else if (node.isRightChild()) {
                node.parent.right = null;
            }
        }
        return old;
    }

    private void resize() {
        //装填因子 = size / table.length
        int len = table.length;
        if (size / len <= DEFAULT_LOAD_FACTOR) {
            return;
        }
        System.out.println("size = " + size);
        System.out.println("length = " + len);
        System.out.println("resize() is doing");
        Node<K, V>[] old = table;
        table = new Node[len << 1];
        //移动所有节点
        for (int i = 0; i < len; i++) {
            if (old[i] == null) {
                continue;
            }
            Queue<Node<K, V>> queue = new LinkedList<>();
            queue.offer(old[i]);
            Node<K, V> node;
            while (!queue.isEmpty()) {
                node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
                //先左右子树入队再移动
                moveNode(node);
            }
        }
    }

    private void moveNode(Node<K, V> newNode) {
        //重置
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;
        //再移动
        int index = index(newNode);
        Node<K, V> root = table[index];
        if (root == null) {
            root = newNode;
            table[index] = root;
            afterPut(root);
            return;
        } else {
            //哈希冲突，添加新的节点到红黑树
            Node<K, V> parent = root;
            Node<K, V> node = root;
            int cmp = 0;
            K k1 = newNode.key;
            int h1 = newNode.hash;
            Node<K, V> result = null;

            do {
                parent = node;
                K k2 = node.key;
                int h2 = node.hash;
                if (h1 > h2) {
                    cmp = 1;
                } else if (h1 < h2) {
                    cmp = -1;
                } else if (k1 != null && k2 != null
                        && k1 instanceof Comparable
                        && k1.getClass() == k2.getClass()
                        && ((Comparable) k1).compareTo(k2) != 0) {
                } else {
                    //不需要扫描
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
                node = cmp > 0 ? node.right : node.left;
            } while (node != null);
            //收尾
            newNode.parent = parent;
            if (cmp > 0) {
                parent.right = newNode;
            } else {
                parent.left = newNode;
            }
            afterPut(newNode);
        }
    }

}
