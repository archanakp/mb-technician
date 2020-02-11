package com.battmobile.battmobiletechnician.incentive_module;

public class IncentivesModel {
    String id;
    String lead_receipt_no;
    String lead;
    String date;

    public String getId() {
        return id;
    }

    public String getLead_receipt_no() {
        return lead_receipt_no;
    }

    public String getLead() {
        return lead;
    }

    public String getIncentive_amount() {
        return incentive_amount;
    }

    public String getDate() {
        return date;
    }

    public IncentivesModel(String id, String lead_receipt_no, String lead, String date, String incentive_amount) {
        this.id = id;
        this.lead_receipt_no = lead_receipt_no;
        this.lead = lead;
        this.date = date;
        this.incentive_amount = incentive_amount;
    }

    String incentive_amount;

}
