package com.example.vbill.bean;

public class Point {
    private String name;
    private int value;

    public Point(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Point() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Point{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
