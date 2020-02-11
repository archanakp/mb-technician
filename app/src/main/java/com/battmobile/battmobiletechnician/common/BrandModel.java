package com.battmobile.battmobiletechnician.common;

public class BrandModel {
    String id, title, mobile, address;
    boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public BrandModel(String id, String title, String mobile, String address) {
        this.id = id;
        this.title = title;
        this.mobile = mobile;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public BrandModel(String id, String title) {
        this.id = id;
        this.title = title;
    }
}