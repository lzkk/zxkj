package com.zxkj.common.practice.mode.create.abstractFactory;

public class FactoryForA implements Produce {
    @Override
    public Food get() {
        return new A();
    }
}