package com.battmobile.battmobiletechnician.vehicle_module.vehicle;

public class VehicleModel {
    String id;
    String vehicle_no;
    String last_reading;
    String previous_reading;
    String service_date;
    String insurance_date;
    String km_completed;

    public String getKm_completed() {
        return km_completed;
    }

    String date;

    public String getId() {
        return id;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public String getLast_reading() {
        return last_reading;
    }

    public String getPrevious_reading() {
        return previous_reading;
    }

    public String getService_date() {
        return service_date;
    }

    public String getInsurance_date() {
        return insurance_date;
    }

    public String getDate() {
        return date;
    }

    public VehicleModel(String id, String vehicle_no, String last_reading, String previous_reading, String service_date, String insurance_date, String km_completed, String date) {
        this.id = id;
        this.vehicle_no = vehicle_no;
        this.last_reading = last_reading;
        this.previous_reading = previous_reading;
        this.service_date = service_date;
        this.insurance_date = insurance_date;
        this.km_completed = km_completed;
        this.date = date;
    }
}
