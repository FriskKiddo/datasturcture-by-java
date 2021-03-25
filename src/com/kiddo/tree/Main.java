package com.kiddo.tree;

import com.kiddo.printer.BinaryTrees;
import com.kiddo.utils.Visitor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * @author FriskKiddo
 */
@SuppressWarnings("all")
public class Main {

    static void test0() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<Integer>();
        Integer[] data = new Integer[]{7, 4, 9, 2, 5, 8, 11, 3};
        for (int i = 0; i < data.length; i++) {
            binarySearchTree.add(data[i]);
        }
        BinaryTrees.println(binarySearchTree);
        binarySearchTree.remove(7);
        BinaryTrees.println(binarySearchTree);
    }

    static void test1() {
        BinarySearchTree<Person> binarySearchTree = new BinarySearchTree<Person>();
        Integer[] data = new Integer[]{7, 4, 9, 2, 5, 8, 11, 3};
        for (int i = 0; i < data.length; i++) {
            binarySearchTree.add(new Person(data[i]));
        }

        BinaryTrees.println(binarySearchTree);
    }

    static void test2() {
        BinarySearchTree<Person> binarySearchTree = new BinarySearchTree<>(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o2.getAge() - o1.getAge();
            }
        });
        Integer[] data = new Integer[]{7, 4, 9, 2, 5, 8, 11, 3};
        for (int i = 0; i < data.length; i++) {
            binarySearchTree.add(new Person(data[i]));
        }
        BinaryTrees.println(binarySearchTree);
    }

    static void test3() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        Integer[] data = new Integer[]{7, 4, 9, 2, 5, 8, 11, 3};
        for (int i = 0; i < data.length; i++) {
            binarySearchTree.add(data[i]);
        }

        System.out.println(binarySearchTree.toString());
    }

    static void test4() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<Integer>();
        Integer[] data = new Integer[]{7, 4, 9, 2, 5, 8, 11, 3, 22, 23, 25, 32, 10};
        for (int i = 0; i < data.length; i++) {
            binarySearchTree.add(data[i]);
        }
        binarySearchTree.preorder(new Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 9;
            }
        });
    }

    static void test5() {
        AVLTree<Integer> avlTree = new AVLTree<>();
        Integer[] data = new Integer[]{7, 4, 9, 2, 5, 8, 11, 3, 22, 23, 25, 32, 10};
        for (int i = 0; i < data.length; i++) {
            avlTree.add(data[i]);
            System.out.println("[" + data[i] + "]");
            BinaryTrees.println(avlTree);
        }
        avlTree.remove(25);
        BinaryTrees.println(avlTree);
        avlTree.remove(22);
        BinaryTrees.println(avlTree);
    }

    static void test6() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100_0000; i++) {
            list.add((int) (Math.random() * 100_0000));
        }
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        AVLTree<Integer> avlTree = new AVLTree<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            bst.add(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            bst.contains(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            bst.remove(list.get(i));
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            avlTree.add(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            avlTree.contains(list.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            avlTree.remove(list.get(i));
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    static void test7() {
        RBTree<Integer> rbTree = new RBTree<Integer>();
        Integer[] data = new Integer[]{55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50};
        for (int i = 0; i < data.length; i++) {
            rbTree.add(data[i]);
//            System.out.println(data[i]);
//            BinaryTrees.println(rbTree);
        }
        BinaryTrees.println(rbTree);
        rbTree.remove(87);
        BinaryTrees.println(rbTree);
        rbTree.remove(55);

        BinaryTrees.println(rbTree);
    }

    static void test8() {
        Trie<Integer> trie = new Trie<>();
        trie.add("dog", 175);
        trie.add("doggy", 160);
        trie.add("come", 150);
        trie.add("coming", 180);
        Integer dog = trie.get("coming");
        System.out.println(dog);
        trie.remove("come");
        System.out.println(trie.get("coming"));
        System.out.println(trie.get("come"));
    }


    public static void main(String[] args) {
        test8();
    }
}
