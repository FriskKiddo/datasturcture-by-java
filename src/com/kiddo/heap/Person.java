package com.kiddo.heap;

import java.util.Objects;

public class Person {
    private int age;
    private float height;
    private String name;

    public Person(int age, float height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hashCode = Integer.hashCode(age);
        hashCode = hashCode * 31 + Float.hashCode(height);
        hashCode = hashCode * 31 + (name == null ? 0 : name.hashCode());
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            //内存地址一致
            return true;
        }
//        if (o == null || !(o instanceof Person)) {
        //无法识别父类与子类
//            return false;
//        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        //比较成员对象
        return age == person.age &&
                Float.compare(person.height, height) == 0 &&
                Objects.equals(name, person.name);
    }
}
