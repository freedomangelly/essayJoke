package com.android.fragmentlibrary.db;

/**
 * @author Created by freed
 *         Created by freed on 2019/2/12.
 *         Date:2019/2/12
 * @description
 */

public class Person {

    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
