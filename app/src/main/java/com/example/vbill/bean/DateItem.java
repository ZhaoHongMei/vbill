package com.example.vbill.bean;

public class DateItem {
    private int number;
    private String dateName;

    public DateItem() {
    }

    public DateItem(int number, String dateName) {
        this.number = number;
        this.dateName = dateName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }
}
