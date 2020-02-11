package com.battmobile.battmobiletechnician.referral;

public class ReferralModel {
    String id, source, name, number,created_by, date;

    public ReferralModel(String id, String source, String name, String number, String created_by, String date) {
        this.id = id;
        this.source = source;
        this.name = name;
        this.number = number;
        this.created_by = created_by;
        this.date = date;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }
}
