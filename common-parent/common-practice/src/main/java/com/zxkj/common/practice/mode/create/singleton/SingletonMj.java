package com.zxkj.common.practice.mode.create.singleton;

/**
 * 枚举式单例
 */
public class SingletonMj {
    public static void main(String[] args) {
        HungryEnumSingleton singleton = HungryEnumSingleton.INSTANCE;
        System.out.println(singleton);
        singleton.print();
    }
}

enum HungryEnumSingleton {
    INSTANCE;

    public void print() {
        System.out.println("这是通过枚举获得的实例");
        System.out.println("HungryEnumSingleton.print()");
    }
}