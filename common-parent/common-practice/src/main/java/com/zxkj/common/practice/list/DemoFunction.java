package com.zxkj.common.practice.list;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @author ：yuhui
 * @date ：Created in 2021/3/16 14:56
 */
public class DemoFunction {

    public static void main(String[] args) {
        method1();
    }

    private static void method1() {
        //① 使用map方法，泛型的第一个参数是转换前的类型，第二个是转化后的类型
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();//获取每个字符串的长度，并且返回
            }
        };

        Stream<String> stream = Stream.of("aaa", "bbbbb", "ccccccv");
        Stream<Integer> stream1 = stream.map(function);
        stream1.forEach(System.out::println);

        System.out.println("********************");
    }


}
