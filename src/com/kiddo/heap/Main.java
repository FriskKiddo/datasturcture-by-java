package com.kiddo.heap;

import com.kiddo.printer.BinaryTrees;

import java.util.Comparator;
import java.util.Random;

public class Main {

    static void test1() {
        //小顶堆找第k大的元素
        //大顶堆找第k小的元素
        MaxHeap<Integer> binaryHeap = new MaxHeap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        int k = 4;
        int[] data = new int[20];
        for (int i = 0; i < 20; i++) {
            data[i] = new Random().nextInt(1000);
            System.out.print(data[i]);
            if (i != 19) {
                System.out.print(", ");
            }
        }
        System.out.println();
        for (int i = 0; i < 20; i++) {
            int tmp = data[i];
            if (binaryHeap.size < k) {
                binaryHeap.add(tmp);
            } else if (tmp < binaryHeap.get()) {
                binaryHeap.replace(tmp);
            }
        }

        BinaryTrees.println(binaryHeap);
    }

    static void test2(int[] array) {

    }

    public static void main(String[] args) {
    }
}
