package com.zxkj.common.practice.mode.create.abstractFactory;

public class FactoryForB implements Produce {
    @Override
    public Food get() {
        return new B();
    }
}