package com.battmobile.battmobiletechnician.vehicle_module.odometer;

public class OdometerModel {
    String id;
    String previous_reading;
    String current_reading;
    String date;

    public OdometerModel(String id, String previous_reading, String current_reading, String date) {
        this.id = id;
        this.previous_reading = previous_reading;
        this.current_reading = current_reading;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getPrevious_reading() {
        return previous_reading;
    }

    public String getCurrent_reading() {
        return current_reading;
    }

    public String getDate() {
        return date;
    }
}
