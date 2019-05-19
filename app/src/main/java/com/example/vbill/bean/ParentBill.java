package com.example.vbill.bean;

import java.io.Serializable;
import java.util.List;

public class ParentBill implements Serializable {
    private String date;
    private List billList;

    public ParentBill(String date, List billList) {
        this.date = date;
        this.billList = billList;
    }

    public String getDate() {
//        Date date = new Date();
//        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);
//        String dateString = df.format(date);
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List getBillList() {
        return billList;
    }

    public void setBillList(List billList) {
        this.billList = billList;
    }
}
