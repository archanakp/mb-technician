package com.battmobile.battmobiletechnician.inventory_module.scrap_batteries;

public class ScrapBatteryModel {
    String id, brand_name, battery_model, size, quantity, battery_image, date;

    public String getId() {
        return id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getSize() {
        return size;
    }


    public String getBattery_image() {
        return battery_image;
    }

    public String getDate() {
        return date;
    }

    public ScrapBatteryModel(String id, String brand_name, String battery_model, String size, String quantity, String battery_image, String date) {
        this.id = id;
        this.brand_name = brand_name;
        this.battery_model = battery_model;
        this.size = size;
        this.quantity = quantity;
        this.battery_image = battery_image;
        this.date = date;
    }

    public String getBattery_model() {
        return battery_model;
    }

    public String getQuantity() {
        return quantity;
    }
}
