package com.battmobile.battmobiletechnician.vehicle_module.service;

public class ServiceModel {
    String id;
    String service_id;
    String km_completed;
    String next_service_km;
    String service_date;
    String next_service_date;
    String receipt_id;
    String receipt_image;

    public String getId() {
        return id;
    }

    public String getService_id() {
        return service_id;
    }

    public String getKm_completed() {
        return km_completed;
    }

    public String getNext_service_km() {
        return next_service_km;
    }

    public String getService_date() {
        return service_date;
    }

    public String getNext_service_date() {
        return next_service_date;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public String getReceipt_image() {
        return receipt_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public ServiceModel(String id, String service_id, String km_completed, String next_service_km, String service_date, String next_service_date, String receipt_id, String receipt_image, String created_at) {
        this.id = id;
        this.service_id = service_id;
        this.km_completed = km_completed;
        this.next_service_km = next_service_km;
        this.service_date = service_date;
        this.next_service_date = next_service_date;
        this.receipt_id = receipt_id;
        this.receipt_image = receipt_image;
        this.created_at = created_at;
    }

    String created_at;
}
