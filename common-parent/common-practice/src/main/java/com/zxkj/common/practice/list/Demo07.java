package com.zxkj.common.practice.list;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Demo07 {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();

        Student s1 = new Student("张三", 21);
        Student s2 = new Student("李四", 19);
        Student s3 = new Student("王五", 18);
        Student s4 = new Student("程六", 17);
        Student s5 = new Student("赵七", 20);

        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        list.add(s5);

    // 筛选
    Student student = list.stream().filter((s) -> s.getSex() < 20).max(Comparator.comparing((s) -> s.getSex()))
            .get();
        System.out.println(JSON.toJSONString(student));
}

}