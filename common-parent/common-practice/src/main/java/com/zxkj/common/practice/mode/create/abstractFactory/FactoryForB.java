package com.zxkj.common.practice.mode.create.abstractFactory;

class FactoryForB implements produce {
    @Override
    public food get() {
        return new B();
    }
}