package com.battmobile.battmobiletechnician.overtime_module.collection;

public class CollectionModel {
    String id, job_receipt_no, created_at, bonus_amount;

    public String getId() {
        return id;
    }

    public String getJob_receipt_no() {
        return job_receipt_no;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getBonus_amount() {
        return bonus_amount;
    }

    public CollectionModel(String id, String job_receipt_no, String created_at, String bonus_amount) {
        this.id = id;
        this.job_receipt_no = job_receipt_no;
        this.created_at = created_at;
        this.bonus_amount = bonus_amount;
    }
}
