package com.zxkj.common.practice.jdk.divide;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuh
 */
public class ConstantPool {

    public static void main(String[] args) throws Exception {
        //字符串常量池
        v1();
        //基本类型包装类常量池
        v2();

        test();
    }

    //字符串常量池
    private static void v1() {
        //对象创建两种方式
        //第一种：创建了几个对象呢？ 1个或2个 第一个对象:如果常量池中已有"abc"直接返回引用，如果不存在就创建一个；第二个对象:在堆内存中new的String对象。
        String s1 = new String("abc");//s1指向堆内存对象的引用地址

        //第二种：堆内存字符常量池（首先检查字符串常量池中是否存在，如果存在则直接引用，不存在添加进入）
        String s2 = "abc";//指向字符串常量池引用
        String s3 = "abc";//指向字符串常量池引用
        System.out.println(s1 == s2);//false
        System.out.println(s2 == s3);//true
        System.out.println("1>>>>>>>>>>>>>>>");

        String s4 = new String("hello");//s4指向堆内存地址的引用地址
        s4.intern();//调用intern方法，查询字符常量池是否存在字符串，如果不存在就放入字符常量池，如果存在返回地址引用
        String s5 = "hello";
        System.out.println(s4 == s5);//false
        System.out.println("2>>>>>>>>>>>>>>>");

        //字符串拼接
        String s6_1 = "123" + "123";//在字符串常量池创建
        String s6_2 = new String("123") + new String("123");//在对内存中创建
        String s7 = "123123";
        System.out.println(s6_1 == s6_2);//false
        System.out.println(s6_1 == s7);//true
        System.out.println(s6_2 == s7);//false
        System.out.println("3>>>>>>>>>>>>>>>");

        String s8 = new String("456") + "4561";//在字符串常量池创建
        String s9 = "4564561";
        System.out.println(s8 == s9);//false
        System.out.println("4>>>>>>>>>>>>>>>");

        final String s10 = "abc";
        final String s11 = new String("abc");
        System.out.println("s1与s10：" + (s1 == s10));// false
        System.out.println("s1与s11：" + (s1 == s11));// false
    }

    //基本类型包装类常量池(-128 - 127)
    private static void v2() {
        Integer i1 = 40;//Java 在编译的时候会直接将代码封装成 Integer i1=Integer.valueOf(40);，从而使用常量池中的对象。
        Integer i2 = 40;//常量池
        Integer i3 = 0;
        Integer i4 = new Integer(40);//这种情况下会在堆中创建新的对象
        Integer i5 = new Integer(40);//堆内存
        Integer i6 = new Integer(0);

        Integer i7 = i2 + i3;//因为+这个操作符不适用于 Integer 对象，首先 i5 和 i6 进行自动拆箱操作，进行数值相加
        Integer i8 = i5 + i6;
        Integer i9 = new Integer(128);
        Integer i10 = new Integer(128);

        Integer i11 = Integer.valueOf(127);//IntegerCache.cache取数
        Integer i12 = Integer.valueOf(127);//IntegerCache.cache取数

        Integer i13 = Integer.valueOf(128);
        Integer i14 = Integer.valueOf(128);

        System.out.println("i1==i2   " + (i1 == i2));//true
        System.out.println("i1==i7   " + (i1 == i7));//true
        System.out.println("i1==i4   " + (i1 == i4));//false
        System.out.println("i4==i5   " + (i4 == i5));//false
        System.out.println("i4==i8   " + (i4 == i8));//false
        System.out.println("40==i8   " + (40 == i8));//true Integer对象无法与数值进行直接比较，所以 i4 自动拆箱转为 int 值 40，最终这条语句转为 40 == 40 进行数值比较。
        System.out.println("i9==i10   " + (i9 == i10));//false
        System.out.println("i11==i12   " + (i11 == i12));//true
        System.out.println("i13==i14   " + (i13 == i14));//false
    }

    private static void test() {
        try {
            List<String> strings = new ArrayList<String>();
            String aa = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            while (true) {
                strings.add(aa.intern());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}