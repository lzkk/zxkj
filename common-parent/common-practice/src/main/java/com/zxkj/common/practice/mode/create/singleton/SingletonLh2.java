package com.zxkj.common.practice.mode.create.singleton;

/**
 * 懒汉模式-线程安全，适用于多线程
 * 在内部类被加载和初始化时 才创建实例
 * 静态内部类不会自动随着外部类的加载和初始化而初始化，它是要单独加载和初始化的。
 * 因为是在内部类加载和初始化时创建的 因此它是线程安全的
 */
public class SingletonLh2 {
    private SingletonLh2() {
        if (Inner.INSTANCE != null) {
            throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取");
        }
    }

    private static class Inner {
        private static final SingletonLh2 INSTANCE = new SingletonLh2();
    }

    public static SingletonLh2 getInstance() {
        return Inner.INSTANCE;
    }

    public static void main(String[] args) {
        System.out.println(SingletonLh2.getInstance());
    }
}