package com.zxkj.common.practice.mode.j2ee.dataAccessObject;

import java.util.ArrayList;
import java.util.List;

class Student3 {
    private String name;
    private int rollNo;

    Student3(String name, int rollNo) {
        this.name = name;
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }
}

interface StudentDao {
    public List<Student3> getAllStudents();

    public Student3 getStudent(int rollNo);

    public void updateStudent(Student3 student);

    public void deleteStudent(Student3 student);
}

class StudentDaoImpl implements StudentDao {

    //列表是当作一个数据库
    List<Student3> students;

    public StudentDaoImpl() {
        students = new ArrayList<>();
        Student3 student1 = new Student3("Robert", 0);
        Student3 student2 = new Student3("John", 1);
        students.add(student1);
        students.add(student2);
    }

    @Override
    public void deleteStudent(Student3 student) {
        students.remove(student.getRollNo());
        System.out.println("Student: Roll No " + student.getRollNo()
                + ", deleted from database");
    }

    //从数据库中检索学生名单
    @Override
    public List<Student3> getAllStudents() {
        return students;
    }

    @Override
    public Student3 getStudent(int rollNo) {
        return students.get(rollNo);
    }

    @Override
    public void updateStudent(Student3 student) {
        students.get(student.getRollNo()).setName(student.getName());
        System.out.println("Student: Roll No " + student.getRollNo()
                + ", updated in the database");
    }
}

public class DaoPatternDemo {
    public static void main(String[] args) {
        StudentDao studentDao = new StudentDaoImpl();

        //输出所有的学生
        for (Student3 student : studentDao.getAllStudents()) {
            System.out.println("Student: [RollNo : " + student.getRollNo() + ", Name : " + student.getName() + " ]");
        }


        //更新学生
        Student3 student = studentDao.getAllStudents().get(0);
        student.setName("Michael");
        studentDao.updateStudent(student);

        //获取学生
        studentDao.getStudent(0);
        System.out.println("Student: [RollNo : " + student.getRollNo() + ", Name : " + student.getName() + " ]");
    }
}