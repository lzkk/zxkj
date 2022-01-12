package com.zxkj.common.practice.mode.create.singleton;

/**
 * 饿汉式(静态块)
 */
public class SingletonEh2 {
    /* 持有私有静态实例，防止被引用 */
    public static final SingletonEh2 instance;

    /* 私有构造方法，防止被实例化 */
    private SingletonEh2() {
        if (SingletonEh2.instance != null) {
            throw new RuntimeException("实例已经存在");
        }
    }

    static {
        instance = new SingletonEh2();
    }

    public static void main(String[] args) {
        System.out.println(SingletonEh2.instance);
    }
}