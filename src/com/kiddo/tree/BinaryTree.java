package com.kiddo.tree;

import com.kiddo.printer.BinaryTreeInfo;
import com.kiddo.utils.Visitor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author FriskKiddo
 */
public class BinaryTree<E> implements BinaryTreeInfo {

    protected Node<E> root;
    protected int size;

    protected static class Node<E> {

        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public boolean hasTwoChildren(){
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChild()) {
                return parent.left;
            }
            return null;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent);
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>) node).right;
    }

    @Override
    public Object string(Object node) {
        return ((Node<E>) node).element;
    }

    protected void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }

    protected Node<E> predecessor(Node<E> node) {
        if (node == null) {
            return null;
        }
        Node<E> p = node.left;
        //前驱节点在左子树中
        if (p != null) {
            while (p.right!= null) {
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

    protected Node<E> successor(Node<E> node) {
        if (node == null) {
            return null;
        }
        //后驱节点在右子树
        Node<E> p = node.right;
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

    /**
     * 遍历
     */
    public void preorder(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        preorder(root, visitor);
    }

    public void preorder(Node<E> node, Visitor<E> visitor) {
        Deque<Node<E>> stack = new ArrayDeque<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            Node<E> temp = stack.pop();
            visitor.stop = visitor.visit(temp.element);
            if (temp.right != null) {
                stack.push(temp.right);
            }
            if (temp.left != null) {
                stack.push(temp.left);
            }
        }
    }

    public void inorder(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        inorder(root, visitor);
    }

    public void inorder(Node<E> node, Visitor<E> visitor) {
        Deque<Node<E>> stack = new ArrayDeque<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            visitor.stop = visitor.visit(node.element);
            node = node.right;
        }
    }

    public void postorder(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        postorder(root, visitor);
    }

    public void postorder(Node<E> node, Visitor<E> visitor) {
        Deque<Node<E>> stack = new ArrayDeque<>();
        Node<E> lastVisited = null;
        Node<E> temp = node;
        while (temp != null || !stack.isEmpty()) {
            while (temp != null) {
                stack.push(temp);
                temp = temp.left;
            }
            if (!stack.isEmpty()) {
                temp = stack.pop();
                if (temp.right == null || temp.right == lastVisited) {
                    visitor.stop = visitor.visit(temp.element);
                    lastVisited = temp;
                    temp = null;
                } else {
                    stack.push(temp);
                    temp = temp.right;
                }
            }
        }
    }

    public void levelOrder(Visitor<E> visitor) {
        if (visitor == null) {
            return;
        }
        levelOrder(root, visitor);
    }

    public void levelOrder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) {
            return;
        }
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(node);
        while (!queue.isEmpty()) {
            node = queue.poll();
            if (visitor.visit(node.element)) {
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
