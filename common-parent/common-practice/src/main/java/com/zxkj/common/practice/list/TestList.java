package com.zxkj.common.practice.list;

import java.util.*;
import java.util.stream.Collectors;

public class TestList {

    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        list.add(new Student("12", 1));
        list.add(new Student("13", 2));
        list.add(new Student("11", 1));
        list.add(new Student("18", 1));
        list.add(new Student("18", 0));
        list.add(new Student("18", 2));
        list.add(new Student("18", 2));
        List<Demo> demos = new ArrayList<Demo>();

//        List<Student> tmpList = Arrays.asList(new Student("111", 1), new Student("112", 2));
        demos = printData(demos, list);
//         printSexequal0(demos);
//         filterAge(demos);
//         sort(demos);
//         pour(demos);
//         pour2(demos);
//         moreSort(demos);
//         morePour(demos);
        groupByAge(demos);

    }

    /**
     * 数据打印
     *
     * @param demos
     * @param list
     */
    public static List<Demo> printData(List<Demo> demos, List<Student> list) {
        demos = list.stream().map(student -> new Demo(student.getAge(), student.getSex())).collect(Collectors.toList());
        demos.forEach(demo ->{
            System.out.println(demo.getAge());
        });
        return demos;
    }

    /**
     * 打印性别为0的数据
     *
     * @param demos
     */
    public static void printSexequal0(List<Demo> demos) {
        List<Demo> collect = demos.stream().filter(demo -> demo.getSex() == 0).distinct().collect(Collectors.toList());
        collect.forEach(item -> {
            System.out.println("\n" + item.getAge() + ":" + item.getSex());
        });
    }

    /**
     * 过滤年龄大于12的信息
     *
     * @param demos
     */
    public static void filterAge(List<Demo> demos) {
        List<Demo> collect = demos.stream().filter(demo -> Integer.valueOf(demo.getAge()) > 12).collect(Collectors.toList());
        collect.forEach(demo -> {
            System.out.println(demo.getAge() + ":" + demo.getSex());
        });
    }

    /**
     * 数据排序
     *
     * @param demos
     */
    public static void sort(List<Demo> demos) {
        List<Demo> collect = demos.stream().sorted((s1, s2) -> s1.getAge().compareTo(s2.getAge())).collect(Collectors.toList());
        collect.forEach(demo -> {
            System.out.println(demo.getAge());
        });
    }

    /**
     * 倒叙
     *
     * @param demos
     */
    public static void pour(List<Demo> demos) {
        ArrayList<Demo> demoArray = (ArrayList<Demo>) demos;
        demoArray.sort(Comparator.comparing(Demo::getAge).reversed());
        demoArray.forEach(demo -> {
            System.out.println(demo.getAge());
        });
    }

    /**
     * 倒叙2
     *
     * @param demos
     */
    public static void pour2(List<Demo> demos) {
        ArrayList<Demo> demoArray = (ArrayList<Demo>) demos;
        Comparator<Demo> comparator = (h1, h2) -> h1.getAge().compareTo(h2.getAge());
        demoArray.sort(comparator.reversed());
        demoArray.forEach(demo -> {
            System.out.println(demo.getAge());
        });
    }

    /**
     * 多条件排序--正序
     *
     * @param demos
     */
    public static void moreSort(List<Demo> demos) {
        demos.sort(Comparator.comparing(Demo::getSex).thenComparing(Demo::getAge));
        demos.forEach(demo -> {
            System.out.println(demo.getSex() + ":" + demo.getAge());
        });
    }

    /**
     * 多条件倒叙
     *
     * @param demos
     */
    public static void morePour(List<Demo> demos) {
        demos.sort(Comparator.comparing(Demo::getSex).reversed().thenComparing(Demo::getAge));
        demos.forEach(demo -> {
            System.out.println(demo.getSex() + ":" + demo.getAge());
        });
    }

    /**
     * 分组
     *
     * @param demos
     */
    public static void groupByAge(List<Demo> demos) {
        Map<String, List<Demo>> collect = demos.stream().collect(Collectors.groupingBy(Demo::getAge));
        collect.forEach((key, value) -> {
            value.forEach(demo -> {
                System.out.println(key + ":" + demo.getSex());
            });
        });
    }
}