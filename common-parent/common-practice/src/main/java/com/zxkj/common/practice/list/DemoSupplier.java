package com.zxkj.common.practice.list;

import java.util.Optional;
import java.util.Random;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @author ：yuhui
 * @date ：Created in 2021/3/16 14:56
 */
public class DemoSupplier {

    public static void main(String[] args) {
//        method1();

        method2();
    }

    private static void method1() {
        //① 使用Supplier接口实现方法,只有一个get方法，无参数，返回一个值
        IntSupplier supplier = new IntSupplier() {
            @Override
            public int getAsInt() {
                //返回一个随机值
                return new Random().nextInt();
            }
        };

        System.out.println(supplier.getAsInt());

        System.out.println("********************");

        //② 使用lambda表达式，
        supplier = () -> new Random().nextInt();
        System.out.println(supplier.getAsInt());
        System.out.println("********************");

        //③ 使用方法引用
        Supplier<Double> supplier2 = Math::random;
        System.out.println(supplier2.get());
    }

    private static void method2() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
        //返回一个optional对象
        Optional<Integer> first = stream.filter(i -> i > 40).findFirst();

        //orElse，如果first中存在数，就返回这个数，如果不存在，就返回传入的数
        System.out.println(first.orElse(1));
        System.out.println(first.orElse(7));

        System.out.println("********************");

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                //返回一个随机值
                return new Random().nextInt();
            }
        };
        //optional对象有需要Supplier接口的方法
        //orElseGet，如果first中存在数，就返回这个数，如果不存在，就返回supplier返回的值
        System.out.println(first.orElseGet(supplier));
    }

}
