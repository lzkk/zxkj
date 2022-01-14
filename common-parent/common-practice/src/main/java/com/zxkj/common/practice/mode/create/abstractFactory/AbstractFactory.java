package com.zxkj.common.practice.mode.create.abstractFactory;

public class AbstractFactory {
    public void ClientCode(String name) {
        Food x = new FactoryForA().get();
        x = new FactoryForB().get();
    }
}