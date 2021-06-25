package com.zxkj.common.practice.list;

import java.util.function.Supplier;

/**
 * TODO
 *
 * @author ：yuhui
 * @date ：Created in 2021/3/16 14:56
 */
public class Demo02 {

    public static void main(String[] args) {
        // 创建字符串
        String str = "hello world";

        // 调用
        String sup = testSup(() -> str);

        System.out.println(sup);
    }

    /**
     * @param sup
     * @return
     */
    public static String testSup(Supplier<String> sup) {
        // 执行
        String s = sup.get();
        return s;
    }

}
