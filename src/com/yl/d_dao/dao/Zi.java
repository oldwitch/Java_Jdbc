package com.yl.d_dao.dao;

public class Zi extends Fu {
    public String name = "luly";
    public int age = 9;
    public Zi() {
        System.out.println(this.age);
        System.out.println(this);
    }

    public static void main(String[] args) {
        new Zi();
    }
}
