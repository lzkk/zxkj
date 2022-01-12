package com.zxkj.common.practice.mode.create.singleton;

/**
 * 饿汉式(构造方法)
 */
public class SingletonEh1 {
    /* 持有私有静态实例，防止被引用 */
    private static SingletonEh1 instance = new SingletonEh1();

    /* 私有构造方法，防止被实例化 */
    private SingletonEh1() {
        if (SingletonEh1.instance != null) {
            throw new RuntimeException("实例已经存在");
        }
    }

    public static SingletonEh1 getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(SingletonEh1.getInstance());
    }
}