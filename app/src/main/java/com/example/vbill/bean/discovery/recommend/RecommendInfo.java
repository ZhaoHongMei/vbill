package com.example.vbill.bean.discovery.recommend;

public class RecommendInfo {

    private Basic basic;
    private RecommendMenu recommendMenu;
    private RecommendContent recommendContent;
    private ContentProperty contentProperty;

    public RecommendInfo() {
    }

    public RecommendInfo(Basic basic, RecommendMenu recommendMenu, RecommendContent recommendContent, ContentProperty contentProperty) {
        this.basic = basic;
        this.recommendMenu = recommendMenu;
        this.recommendContent = recommendContent;
        this.contentProperty = contentProperty;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public RecommendMenu getRecommendMenu() {
        return recommendMenu;
    }

    public void setRecommendMenu(RecommendMenu recommendMenu) {
        this.recommendMenu = recommendMenu;
    }

    public RecommendContent getRecommendContent() {
        return recommendContent;
    }

    public void setRecommendContent(RecommendContent recommendContent) {
        this.recommendContent = recommendContent;
    }

    public ContentProperty getContentProperty() {
        return contentProperty;
    }

    public void setContentProperty(ContentProperty contentProperty) {
        this.contentProperty = contentProperty;
    }
}
