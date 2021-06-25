package com.zxkj.common.practice.list;

import java.util.function.Consumer;

public class Demo01 {
    public static void main(String[] args) {
        // 创建字符串
        String str = "hello world";

        // 调用
        testCon(str, (s) -> System.out.println(s));
    }

    /**
     * @param str 传入参数
     * @param con
     */
    public static void testCon(String str, Consumer<String> con) {
        // 执行
        con.accept(str);
    }

}