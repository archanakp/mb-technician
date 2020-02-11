package com.battmobile.battmobiletechnician.inventory_module.scrap_batteries;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Awesome Pojo Generator
 */
public class SelectProductModel {

    String id, title, size;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSize() {
        return size;
    }

    public SelectProductModel(String id, String title, String size) {
        this.id = id;
        this.title = title;
        this.size = size;
    }
}