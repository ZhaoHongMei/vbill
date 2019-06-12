package com.example.vbill.bean;

public class Duration {
    private String startDay;
    private String endDay;

    public Duration(String startDay, String endDay) {
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public Duration() {
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "startDay='" + startDay + '\'' +
                ", endDay='" + endDay + '\'' +
                '}';
    }
}
