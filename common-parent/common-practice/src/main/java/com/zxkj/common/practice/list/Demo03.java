package com.zxkj.common.practice.list;

import java.util.function.Predicate;

/**
 * TODO
 *
 * @author ：yuhui
 * @date ：Created in 2021/3/16 14:56
 */
public class Demo03 {

    public static void main(String[] args) {
        // 创建字符串
        String str = "hello world";

        // 调用
        boolean flag = testPre(str, (s) -> s.length() > 0);

        System.out.println(flag);
    }

    /**
     * @param str
     * @param pre
     * @return
     */
    public static boolean testPre(String str, Predicate<String> pre) {
        // 执行
        boolean flag = pre.test(str);

        return flag;
    }


}
