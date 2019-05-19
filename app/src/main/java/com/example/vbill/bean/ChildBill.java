package com.example.vbill.bean;

import java.io.Serializable;

public class ChildBill implements Serializable {
    private String category;
    private String amounts;
    private int image;
    private String type;

    public ChildBill(String category, String amounts, String type, int image){
        this.amounts = amounts;
        this.category = category;
        this.type = type;
        this.image = image;
    }


    public String getCategory() {
        return category;
    }

    public String getAmounts() {
        return amounts;
    }

    public int getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("this amount is "+this.getAmounts()+";;;;;;");
        stringBuffer.append("this category is " + this.getCategory()+";;;;;;");
        stringBuffer.append("this type is " + this.getType()+";;;;;;");
        stringBuffer.append("this imageID is " + String.valueOf(this.getImage()));
        return stringBuffer.toString();
    }
}
