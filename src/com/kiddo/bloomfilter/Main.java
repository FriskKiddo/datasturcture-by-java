package com.kiddo.bloomfilter;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

public class Main {
    public static void main(String[] args) {


    }



    static void test1() {
        String[] urls = {};
        BloomFilter<String> bf = new BloomFilter<>(100000000, 0.01);
        System.out.println(Integer.MAX_VALUE);
    }

    static void test2() {

        Deque<Integer> deque = new LinkedList<>();
        deque.peek();

    }

    static void test3() throws IOException {
        File file = new File("F://file.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int)file.length());
        channel.read(buffer);
        System.out.println(new String(buffer.array()));
        fileInputStream.close();
    }

    static void test4() throws IOException {
        String str = "hello world!!!";
        File file = new File("F://file.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(str.getBytes());
        buffer.flip();
        channel.write(buffer);
        fileOutputStream.close();
    }


}
