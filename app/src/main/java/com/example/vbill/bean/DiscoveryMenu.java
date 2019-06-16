package com.example.vbill.bean;

public class DiscoveryMenu {
    private int discoveryMenuImage;
    private String discoveryMenuName;

    public DiscoveryMenu(int discoveryMenuImage, String discoveryMenuName) {
        this.discoveryMenuImage = discoveryMenuImage;
        this.discoveryMenuName = discoveryMenuName;
    }

    public int getDiscoveryMenuImage() {
        return discoveryMenuImage;
    }

    public String getDiscoveryMenuName() {
        return discoveryMenuName;
    }

    public void setDiscoveryMenuImage(int discoveryMenuImage) {
        this.discoveryMenuImage = discoveryMenuImage;
    }

    public void setDiscoveryMenuName(String discoveryMenuName) {
        this.discoveryMenuName = discoveryMenuName;
    }
}
