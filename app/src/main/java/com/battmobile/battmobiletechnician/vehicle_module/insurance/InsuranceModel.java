package com.battmobile.battmobiletechnician.vehicle_module.insurance;

public class InsuranceModel {
    String id, insurance_id, insurance_date, renew_date, receipt_id, receipt_image, created_at;

    public String getId() {
        return id;
    }

    public String getInsurance_id() {
        return insurance_id;
    }

    public String getInsurance_date() {
        return insurance_date;
    }

    public String getRenew_date() {
        return renew_date;
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

    public InsuranceModel(String id, String insurance_id, String insurance_date, String renew_date, String receipt_id, String receipt_image, String created_at) {
        this.id = id;
        this.insurance_id = insurance_id;
        this.insurance_date = insurance_date;
        this.renew_date = renew_date;
        this.receipt_id = receipt_id;
        this.receipt_image = receipt_image;
        this.created_at = created_at;
    }
}
