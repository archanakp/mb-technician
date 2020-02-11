package com.battmobile.battmobiletechnician.inventory_module.assigned_batteries;

public class AssignedBatteryModel {
    String id, brand_name, size, remaining_quantity, assigned_quantity, battery_image, date;

    public String getId() {
        return id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getSize() {
        return size;
    }

    public String getRemaining_quantity() {
        return remaining_quantity;
    }

    public String getAssigned_quantity() {
        return assigned_quantity;
    }

    public String getBattery_image() {
        return battery_image;
    }

    public String getDate() {
        return date;
    }

    public AssignedBatteryModel(String id, String brand_name, String size, String remaining_quantity, String assigned_quantity, String battery_image, String date) {
        this.id = id;
        this.brand_name = brand_name;
        this.size = size;
        this.remaining_quantity = remaining_quantity;
        this.assigned_quantity = assigned_quantity;
        this.battery_image = battery_image;
        this.date = date;
    }
}
