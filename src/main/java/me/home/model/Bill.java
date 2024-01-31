package me.home.model;

import me.home.enums.BillState;
import me.home.enums.BillType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bill {
    private long billId;
    private BillType type;
    private double amount;
    private Date dueDate;
    private String provider;
    private BillState state;

    public Bill(long billId, BillType type, double amount, Date dueDate, String providers) {
        this.billId = billId;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.provider = providers;
        this.state = BillState.NOT_PAID;
    }

    public long getBillId() {
        return billId;
    }

    public BillType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getProvider() {
        return provider;
    }

    public BillState getState() {
        return state;
    }

    public void setState(BillState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("billId", billId);
        data.put("type", type);
        data.put("amount", amount);
        data.put("dueDate", new SimpleDateFormat("dd/MM/yyyy").format(dueDate));
        data.put("state", state);
        data.put("provider", provider);
        return data.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Bill other)) {
            return false;
        }
        return this.billId == other.getBillId();
    }
}
