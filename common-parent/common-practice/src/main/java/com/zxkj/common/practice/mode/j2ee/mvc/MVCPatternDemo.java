package com.zxkj.common.practice.mode.j2ee.mvc;

class Student2 {
    private String rollNo;
    private String name;

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class StudentView {
    public void printStudentDetails(String studentName, String studentRollNo) {
        System.out.println("Student: ");
        System.out.println("Name: " + studentName);
        System.out.println("Roll No: " + studentRollNo);
    }
}

class StudentController {
    private Student2 model;
    private StudentView view;

    public StudentController(Student2 model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    public void setStudentName(String name) {
        model.setName(name);
    }

    public String getStudentName() {
        return model.getName();
    }

    public void setStudentRollNo(String rollNo) {
        model.setRollNo(rollNo);
    }

    public String getStudentRollNo() {
        return model.getRollNo();
    }

    public void updateView() {
        view.printStudentDetails(model.getName(), model.getRollNo());
    }
}

public class MVCPatternDemo {
    public static void main(String[] args) {
        //从数据库获取学生记录
        Student2 model = retrieveStudentFromDatabase();
        //创建一个视图：把学生详细信息输出到控制台
        StudentView view = new StudentView();
        StudentController controller = new StudentController(model, view);
        controller.updateView();
        //更新模型数据
        controller.setStudentName("John");
        controller.updateView();
    }

    private static Student2 retrieveStudentFromDatabase() {
        Student2 student = new Student2();
        student.setName("Robert");
        student.setRollNo("10");
        return student;
    }
}