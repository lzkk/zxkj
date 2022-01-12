package com.zxkj.common.practice.mode.create.abstractFactory;

class FactoryForA implements produce {
    @Override
    public food get() {
        return new A();
    }
}