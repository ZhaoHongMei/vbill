package com.example.vbill.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChildBill implements Serializable {
    private String itemId;
    private String createDay;
    private String createTime;
    private String categoryCode;
    private String categoryDesc;
    private String amount;
    private String imagePath;
    private String type;


    public ChildBill(String itemId, String createDay, String createTime, String imagePath,String categoryCode,String categoryDesc,String type,String amount){
        this.itemId = itemId;
        this.createDay = createDay;
        this.createTime = createTime;
        this.imagePath = imagePath;
        this.categoryCode = categoryCode;
        this.categoryDesc = categoryDesc;
        this.type = type;
        this.amount = amount;
    }
    public ChildBill(Map childMap ){
        this.itemId = String.valueOf(childMap.get("itemId"));
        this.createDay = String.valueOf(childMap.get("createDay"));
        this.createTime = String.valueOf(childMap.get("createTime"));
        this.imagePath = String.valueOf(childMap.get("imagePath"));
        this.categoryCode = String.valueOf(childMap.get("categoryCode"));
        this.categoryDesc = String.valueOf(childMap.get("categoryDesc"));
        this.type =String.valueOf(childMap.get("type"));
        this.amount = String.valueOf(childMap.get("amount"));
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getAmount() {
        if("out".equalsIgnoreCase(getType())){
            return "-" + amount;
        }
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("this itemId is "+this.getItemId()+";;;;;;");
        stringBuffer.append("this createDay is " + this.getCreateDay()+";;;;;;");
        stringBuffer.append("this createTime is " + this.getCreateTime()+";;;;;;");
        stringBuffer.append("this categoryCode is " + this.getCategoryCode()+";;;;;");
        stringBuffer.append("this categoryDesc is " + this.getCategoryDesc()+";;;;;");
        stringBuffer.append("this amount is " + this.getAmount()+";;;;;");
        stringBuffer.append("this imagePath is " + this.getImagePath()+";;;;;");
        stringBuffer.append("this type is " + this.getType()+";;;;;");
        return stringBuffer.toString();
    }
}
