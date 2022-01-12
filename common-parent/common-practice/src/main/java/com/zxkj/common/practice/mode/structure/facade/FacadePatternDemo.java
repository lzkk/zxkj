package com.zxkj.common.practice.mode.structure.facade;

interface Shape {
    void draw();
}

class Rectangle3 implements Shape {

    @Override
    public void draw() {
        System.out.println("Rectangle::draw()");
    }
}

class Square3 implements Shape {

    @Override
    public void draw() {
        System.out.println("Square::draw()");
    }
}

class Circle3 implements Shape {

    @Override
    public void draw() {
        System.out.println("Circle::draw()");
    }
}

class ShapeMaker {
    private Shape circle;
    private Shape rectangle;
    private Shape square;

    public ShapeMaker() {
        circle = new Circle3();
        rectangle = new Rectangle3();
        square = new Square3();
    }

    public void drawCircle() {
        circle.draw();
    }

    public void drawRectangle() {
        rectangle.draw();
    }

    public void drawSquare() {
        square.draw();
    }
}

public class FacadePatternDemo {
    public static void main(String[] args) {
        ShapeMaker shapeMaker = new ShapeMaker();

        shapeMaker.drawCircle();
        shapeMaker.drawRectangle();
        shapeMaker.drawSquare();
    }
}