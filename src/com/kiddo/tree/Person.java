package com.kiddo.tree;

/**
 * @author FriskKiddo
 */
public class Person implements Comparable<Person> {

    private int age;

    public Person(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(Person o) {
        return age - o.age;
    }

    @Override
    public String toString() {
        return "Person_" +
                "age=" + age;
    }
}
