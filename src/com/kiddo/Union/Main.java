package com.kiddo.Union;

public class Main {
    public static void main(String[] args) {
        GenericUnionFind<Student> guf = new GenericUnionFind<>();
        Student rose = new Student(12, "rose");
        Student jack = new Student(19, "jack");
        Student mike = new Student(19, "mike");
        Student frisk = new Student(19, "frisk");
        guf.makeSet(rose);
        guf.makeSet(jack);
        guf.makeSet(mike);
        guf.makeSet(frisk);
        guf.union(rose, jack);
        guf.union(mike, frisk);
        System.out.println(guf.isSame(rose, jack));
        System.out.println(guf.isSame(rose, mike));
        guf.union(rose, frisk);
        System.out.println(guf.isSame(rose, mike));

    }
}
