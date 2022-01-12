package com.zxkj.common.practice.mode.create.singleton;

/**
 * 懒汉式
 */
public class SingletonLh1 {
    /* 防止指令重排 */
    private static volatile SingletonLh1 instance = null;

    /* 私有构造方法，防止被实例化 */
    private SingletonLh1() {
        if (SingletonLh1.instance != null) {
            throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取");
        }
    }

    public static SingletonLh1 getInstance() {
        if (instance == null) {
            // double check
            synchronized (SingletonLh1.class) {
                if (instance == null) {
                    instance = new SingletonLh1();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(SingletonLh1.getInstance());
    }
}