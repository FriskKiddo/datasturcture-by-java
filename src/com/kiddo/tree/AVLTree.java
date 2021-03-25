package com.kiddo.tree;

import org.omg.CORBA.NO_IMPLEMENT;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * @author FriskKiddo
 */
@SuppressWarnings("all")
public class AVLTree<E> extends BalancedBinarySearchTree<E> {

    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }

    private class AVLNode<E> extends Node<E> {

        int height = 1;

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        public int balanceFactor() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            return leftHeight - rightHeight;
        }

        public void updateHeight() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            height = 1 + Math.max(leftHeight, rightHeight);
        }

        public Node<E> tallerChild() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            if (leftHeight > rightHeight) {
                return left;
            } else if (leftHeight < rightHeight) {
                return right;
            }
            return isLeftChild() ? left : right;
        }

        @Override
        public String toString() {
            String parentString = "null";
            if (parent != null) {
                parentString = parent.element.toString();
            }
            return element + "_p(" + parentString + ")_h(" + height + ")";
        }
    }

    public int height() {
        int height = 0;
        LinkedList<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        int levelSize = 1;
        Node<E> node = null;
        while (!queue.isEmpty()) {
            node = queue.poll();
            levelSize--;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (levelSize == 0) {
                //访问下一层
                levelSize = queue.size();
                height++;
            }
        }
        return height;
    }

    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }

    private boolean isBalanced(Node<E> node) {
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1;
    }

    private void rebalance(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        Node<E> child = ((AVLNode<E>) parent).tallerChild();
        if (parent.isLeftChild()) {
            if (child.isLeftChild()) {
                rotateRight(grand);
            } else {
                rotateLeft(parent);
                rotateRight(grand);
            }
        } else {
            if (child.isLeftChild()) {
                rotateRight(parent);
                rotateLeft(grand);
            } else {
                rotateLeft(grand);
            }
        }
    }

    /**
     * 恢复平衡
     *
     * @param grand 高度最低的不平衡节点(grandparent)
     */
    private void rebalance1(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>) grand).tallerChild();
        Node<E> node = ((AVLNode<E>) parent).tallerChild();
        if (parent.isLeftChild()) {
            if (node.isLeftChild()) {
                //LL
                rotate(grand, node.left, node, node.right, parent, parent.right, grand, grand.right);
            } else {
                //LR
                rotate(grand, parent.left, parent, node.left, node, node.right, grand, grand.right);
            }
        } else {
            if (node.isRightChild()) {
                //RR
                rotate(grand, grand.left, grand, parent.left, parent, node.left, node, node.right);
            } else {
                //RL
                rotate(grand, grand.left, grand, node.left, node, node.right, parent, parent.right);
            }
        }
    }

    @Override
    protected void rotate(Node<E> r, Node<E> a, Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f, Node<E> g) {
        super.rotate(r, a, b, c, d, e, f, g);
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    @Override
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);
        //从下到上更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    @Override
    protected void afterAdd(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                //更新高度
                updateHeight(node);
            } else {
                //恢复平衡
                rebalance(node);
                break;
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                updateHeight(node);
            } else {
                rebalance(node);
            }
        }
    }
}
