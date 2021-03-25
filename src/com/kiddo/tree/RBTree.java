package com.kiddo.tree;

import java.util.Comparator;

/**
 * @author FriskKiddo
 */
@SuppressWarnings("all")
public class  RBTree<E> extends BalancedBinarySearchTree<E> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RBTree() {
        this(null);
    }

    public RBTree(Comparator<E> comparator) {
        super(comparator);
    }

    public Node<E> color(Node<E> node, boolean color) {
        if (node == null) {
            return node;
        }
        ((RBNode<E>) node).color = color;
        return node;
    }

    private Node<E> red(Node<E> node) {
        return color(node, RED);
    }

    private Node<E> black(Node<E> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>) node).color;
    }

    private boolean isBlack(Node<E> node) {
        return colorOf(node) == BLACK;
    }

    private boolean isRed(Node<E> node) {
        return colorOf(node) == RED;
    }

    private static class RBNode<E> extends Node<E> {

        boolean color = RED;

        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element.toString();
        }
    }

    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent = node.parent;
        if (parent == null) {
            //添加的是根节点
            black(node);
        }
        if (isBlack(parent)) {
            //若父节点是黑色
            return;
        }
        //判断uncle节点
        Node<E> uncle = node.parent.sibling();
        Node<E> grand = parent.parent;
        if (isRed(uncle)) {
            //uncle节点是红色
            black(parent);
            black(uncle);
            //祖父节点向上合并
            afterAdd(red(grand));
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

    /**
     * 删除node后的调整
     *
     * @param node 被删除的节点或者用以取代被删除节点的子节点（当被删除节点的度为1）
     */
    @Override
    protected void afterRemove(Node<E> node) {
        if (isRed(node)) {
            //删除节点是红色或者用以取代删除的节点是红色
            black(node);
            return;
        }
        Node<E> parent = node.parent;
        if (parent == null) {
            return;
        }
        //删除黑色叶子节点【下溢】
        //判断被删除的node是左是右
        boolean left = parent.left == null || node.isLeftChild(); //     初始情况，通过node.parent的孩子指针判断||递归情况
        Node<E> sibling = left ? parent.right : parent.left;
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

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode<>(element, parent);
    }
}
