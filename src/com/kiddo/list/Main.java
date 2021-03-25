package com.kiddo.list;

import com.kiddo.tree.Person;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<Person> queue = new PriorityQueue<>(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o2.getAge() - o1.getAge();
            }
        });

        Person p1 = new Person(23);
        Person p2 = new Person(56);
        Person p3 = new Person(12);
        Person p4 = new Person(33);

        queue.enQueue(p1);
        queue.enQueue(p2);
        queue.enQueue(p3);
        queue.enQueue(p4);
        while (!queue.isEmpty()) {
            System.out.println(queue.deQueue().getAge());
        }

    }
}
