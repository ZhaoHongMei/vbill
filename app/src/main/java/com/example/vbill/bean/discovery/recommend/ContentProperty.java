package com.example.vbill.bean.discovery.recommend;

public class ContentProperty {
    private String thumbUpTimes;
    private String commentTimes;

    public ContentProperty() {
    }

    public ContentProperty(String thumbUpTimes, String commentTimes) {
        this.thumbUpTimes = thumbUpTimes;
        this.commentTimes = commentTimes;
    }

    public String getThumbUpTimes() {
        return thumbUpTimes;
    }

    public void setThumbUpTimes(String thumbUpTimes) {
        this.thumbUpTimes = thumbUpTimes;
    }

    public String getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(String commentTimes) {
        this.commentTimes = commentTimes;
    }
}
