package com.battmobile.battmobiletechnician.job_module;

public class JobModel {
    String id, receipt_no, customer_name, location, date, job_status;

    public String getId() {
        return id;
    }

    public String getReceipt_no() {
        return receipt_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getJob_status() {
        return job_status;
    }

    public JobModel(String id, String receipt_no, String customer_name, String location, String date, String job_status) {
        this.id = id;
        this.receipt_no = receipt_no;
        this.customer_name = customer_name;
        this.location = location;
        this.date = date;
        this.job_status = job_status;
    }
}
