package com.kiddo.set;

import com.kiddo.utils.FileInfo;
import com.kiddo.utils.Files;

public class Main {

    static void test1() {
        FileInfo fileInfo = Files.read("D:\\2_ChenLab\\2_Workspace\\5_IDEAProject\\Mybatis", new String[]{"java"});

        Set<String> listSet = new ListSet<>();
        String[] words = fileInfo.words();
        System.out.println(words.length);
        for (int i = 0; i < words.length; i++) {
            listSet.add(words[i]);
        }
        System.out.println(listSet.size());
    }




    public static void main(String[] args) {
        test1();
    }
}
