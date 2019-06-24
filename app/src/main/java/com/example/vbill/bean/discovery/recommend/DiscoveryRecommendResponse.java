package com.example.vbill.bean.discovery.recommend;

import java.util.List;

public class DiscoveryRecommendResponse {

    private int statusCode;
    private List<RecommendInfo> recommendInfo;

    public DiscoveryRecommendResponse() {
    }

    public DiscoveryRecommendResponse(int statusCode, List<RecommendInfo> recommendInfo) {
        this.statusCode = statusCode;
        this.recommendInfo = recommendInfo;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<RecommendInfo> getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(List<RecommendInfo> recommendInfo) {
        this.recommendInfo = recommendInfo;
    }
}
