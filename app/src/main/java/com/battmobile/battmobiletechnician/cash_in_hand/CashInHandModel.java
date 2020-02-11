package com.battmobile.battmobiletechnician.cash_in_hand;

public class CashInHandModel {
    String id, agent_name, tranferred_to_agent, amount, date;
    boolean transferred_cash_received;

    public String getId() {
        return id;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public String getTranferred_to_agent() {
        return tranferred_to_agent;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public CashInHandModel(String id, String agent_name, String tranferred_to_agent, String amount, String date, boolean transferred_cash_received) {
        this.id = id;
        this.agent_name = agent_name;
        this.tranferred_to_agent = tranferred_to_agent;
        this.amount = amount;
        this.date = date;
        this.transferred_cash_received = transferred_cash_received;
    }

    public boolean isTransferred_cash_received() {
        return transferred_cash_received;
    }
}
