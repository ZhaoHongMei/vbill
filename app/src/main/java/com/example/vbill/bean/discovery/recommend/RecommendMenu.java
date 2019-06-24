package com.example.vbill.bean.discovery.recommend;

public class RecommendMenu {
    private String cid;
    private String imagePath;
    private String description1;
    private String description2;

    public RecommendMenu() {
    }

    public RecommendMenu(String imagePath, String description1, String description2) {
        this.imagePath = imagePath;
        this.description1 = description1;
        this.description2 = description2;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
