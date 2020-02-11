package com.battmobile.battmobiletechnician.overtime_module.overtime;

public class OvertimeModel {
    String id, receipt_no, bonus_amount, status, date;

    public String getId() {
        return id;
    }

    public String getReceipt_no() {
        return receipt_no;
    }

    public String getBonus_amount() {
        return bonus_amount;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public OvertimeModel(String id, String receipt_no, String bonus_amount, String status, String date) {
        this.id = id;
        this.receipt_no = receipt_no;
        this.bonus_amount = bonus_amount;
        this.status = status;
        this.date = date;
    }
}
