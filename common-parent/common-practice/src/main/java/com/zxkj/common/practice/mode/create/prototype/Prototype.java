package com.zxkj.common.practice.mode.create.prototype;

public class Prototype implements Cloneable {
    private String name;

    public Prototype(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public static void main(String[] args) {
        int count = 20000000;
        long now2 = System.currentTimeMillis();
        Prototype pro = null;
        for (int k = 0; k < count; k++) {
            if (k == 0) {
                pro = new Prototype("" + k);
                pro.getName();

            } else {
                Prototype pro1 = (Prototype) pro.clone();
                pro1.getName();

            }
        }
        System.out.println("cost2:" + (System.currentTimeMillis() - now2));
//        long now = System.currentTimeMillis();
//        Prototype pro22 = null;
//        for (int k = 0; k < count; k++) {
//            if (k == 0) {
//                pro22 = new Prototype("" + k);
//                pro22.getName();
//            } else {
//                pro22 = new Prototype("" + k);
//                pro22.getName();
//            }
//        }
//        System.out.println("cost1:" + (System.currentTimeMillis() - now));

    }
}