package com.zxkj.common.practice.list;

import com.alibaba.fastjson.JSON;

import java.util.*;
import java.util.stream.Collectors;

class Demo {
    private String name;
    private String age;
    private Integer sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Demo(String age, Integer sex) {
        super();
        this.age = age;
        this.sex = sex;
    }
}

class Student {
    private String age;
    private Integer sex;

    public Student(String age, Integer sex) {
        this.age = age;
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}

public class ListStreamDemo {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("aa", "bbb", "ccc");
        // 统计长度大于2的
        long count = list.stream().filter((s) -> s.length() > 2).count();
        System.out.println(count);

        // 将符合要求的放入集合
        List<String> list2 = list.stream().filter((s) -> s.length() > 2).collect(Collectors.toList());
        list2.forEach(System.out::println);

        // 获取最大值
        int max = list.stream().map((s) -> s.length()).max(Integer::compareTo).get();
        System.out.println(max);

        // 获取最小值，另一种方法
        int min = list.stream().min(Comparator.comparing((s) -> s.length())).get().length();
        System.out.println(min);


        List<Long> list3 = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            list3.add(i);
        }
        // reduce：参1，和的初始值
        Long sum = list3.stream().parallel().reduce(0L, (r, l) -> r + l);
        System.out.println(sum);

        List<Student> list5 = new ArrayList<>();
        Student s1 = new Student("张三", 21);
        Student s2 = new Student("李四", 19);
        Student s3 = new Student("王五", 18);
        Student s4 = new Student("程六", 17);
        Student s5 = new Student("赵七", 20);

        list5.add(s1);
        list5.add(s2);
        list5.add(s3);
        list5.add(s4);
        list5.add(s5);

        Student student = list5.stream().filter(s -> s.getSex() < 20).max(Comparator.comparing((s) -> s.getSex())).get();
        System.out.println(JSON.toJSONString(student));


        List<Student> list22 = new ArrayList<>();
        list22.add(new Student("12", 1));
        list22.add(new Student("13", 2));
        list22.add(new Student("11", 1));
        list22.add(new Student("18", 1));
        list22.add(new Student("18", 0));
        list22.add(new Student("18", 2));
        list22.add(new Student("18", 2));
        List<Demo> demos = printData(list22);
        printSexEqual0(demos);
        filterAge(demos);
        sort(demos);
        pour(demos);
        pour2(demos);
        moreSort(demos);
        morePour(demos);
        groupByAge(demos);

    }

    /**
     * 数据打印
     *
     * @param list
     */
    public static List<Demo> printData(List<Student> list) {
        List<Demo> demos = list.stream().map(student -> new Demo(student.getAge(), student.getSex())).collect(Collectors.toList());
        demos.forEach(demo -> {
            System.out.println(demo.getAge());
        });
        return demos;
    }

    /**
     * 打印性别为0的数据
     *
     * @param demos
     */
    public static void printSexEqual0(List<Demo> demos) {
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