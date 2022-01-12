package com.zxkj.common.practice.mode.create.factory;

public class FruitFactory {
    public Fruit produce(String type) {
        if (type.equals("apple")) {
            return new Apple();
        } else if (type.equals("orange")) {
            return new Orange();
        } else {
            System.out.println("请输入正确的类型!");
            return null;
        }
    }
}