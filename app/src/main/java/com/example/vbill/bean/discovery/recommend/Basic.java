package com.example.vbill.bean.discovery.recommend;

public class Basic {
    private String cid;
    private String categoryCode;
    private String author;
    private String updateDate;

    public Basic() {
    }

    public Basic(String cid, String categoryCode, String author, String updateDate) {
        this.cid = cid;
        this.categoryCode = categoryCode;
        this.author = author;
        this.updateDate = updateDate;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
