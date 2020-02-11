package com.battmobile.battmobiletechnician.expense_module;

public class ExpenseModel {
    String id, amount, purpose, description, receipt_image, date, user_id;

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getDescription() {
        return description;
    }

    public String getReceipt_image() {
        return receipt_image;
    }

    public String getDate() {
        return date;
    }

    public String getUser_id() {
        return user_id;
    }

    public ExpenseModel(String id, String amount, String purpose, String description, String receipt_image, String date, String user_id) {
        this.id = id;
        this.amount = amount;
        this.purpose = purpose;
        this.description = description;
        this.receipt_image = receipt_image;
        this.date = date;
        this.user_id = user_id;
    }
}
