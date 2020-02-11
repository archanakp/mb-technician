package com.battmobile.battmobiletechnician.sold_batteries;

public class SoldBatteriesModel {
    String id, brand_name, battery_model, battery_size, price, date;

    public SoldBatteriesModel(String id, String brand_name, String battery_model, String battery_size, String price, String date) {
        this.id = id;
        this.brand_name = brand_name;
        this.battery_model = battery_model;
        this.battery_size = battery_size;
        this.price = price;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getBattery_model() {
        return battery_model;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getBattery_size() {
        return battery_size;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
