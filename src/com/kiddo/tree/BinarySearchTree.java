package com.kiddo.tree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author FriskKiddo
 */
@SuppressWarnings("all")
public class BinarySearchTree<E> extends BinaryTree<E> {

    private Comparator<E> comparator;

    /**
     * node节点的比较规则
     * @param e1
     * @param e2
     * @return
     */
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        } else {
            return ((Comparable<E>) e1).compareTo(e2);
        }
    }

    public BinarySearchTree() {
        this(null);
    }

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    private Node<E> node(E element) {
        Node<E> node = root;
        while (node != null) {
            int cmp = compare(element, node.element);
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

    public boolean contains(E element) {
        return node(element) != null;
    }

    public void add(E e) {
        elementNotNullCheck(e);

        //添加的是第一个元素
        if (root == null) {
            root = createNode(e, null);
            size++;
            afterAdd(root);
            return;
        } else {
            //添加的不是第一个元素
            Node<E> node = root;
            Node<E> parent = null;
            int cmp = 0;
            while (node != null) {
                parent = node;
                cmp = compare(e, node.element);
                if (cmp > 0) {
                    node = node.right;
                } else if (cmp < 0) {
                    node = node.left;
                } else {
                    //重复值
                    node.element = e;
                    return;
                }
            }
            Node<E> newNode = createNode(e, parent);
            if (cmp > 0) {
                parent.right = newNode;
            } else {
                parent.left = newNode;
            }
            size++;
            afterAdd(newNode);
        }
    }

    protected void afterAdd(Node<E> node) {

    }

    protected void afterRemove(Node<E> node) {

    }


    public void remove(E element) {
        remove(node(element));
    }

    private void remove(Node<E> node) {
        if (node == null) {
            return;
        }
        size--;
        //度为2
        if (node.hasTwoChildren()) {
            Node<E> pred = predecessor(node);
            node.element = pred.element;
            node = pred;
        }

        //统一化，删除node节点（node的度必然为1或者0）
        Node<E> replacement = node.left != null ? node.left : node.right;
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
            afterRemove(node);
        } else {
            //叶子节点且非根节点  node的parent指针存在
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {
                node.parent.right = null;
            }
            afterRemove(node);
        }
    }



    /**
     * 判断是否是一个完全二叉树
     * @return
     */
    public boolean isComplete() {
        if (root == null) {
            return false;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        Node<E> node = null;
        while (!queue.isEmpty()) {
            node = queue.poll();
            if (leaf && !node.isLeaf()) {
                return false;
            }
            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) {
                return false;
            }
            if (node.right != null) {
                queue.offer(node.right);
            } else {
                leaf = true;
            }
        }
        return true;
    }

    public void invert() {
        invert(root);
    }

    public void invert(Node<E> node) {
        if (node == null) {
            return;
        }
        Node<E> tmp = node.left;
        node.left = node.right;
        node.right = tmp;
        invert(node.left);
        invert(node.right);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb, "");
        return sb.toString();
    }

    private void toString(Node<E> node, StringBuilder stringBuilder, String prefix) {
        if (node == null) {
            return;
        }
        stringBuilder.append(prefix).append(node.element).append("\n");
        toString(node.left, stringBuilder, prefix + "L ");
        toString(node.right, stringBuilder, prefix + "R ");
    }
}
