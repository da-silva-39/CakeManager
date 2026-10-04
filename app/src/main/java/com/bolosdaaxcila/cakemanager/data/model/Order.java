package com.bolosdaaxcila.cakemanager.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String customerName;
    private String customerPhone;
    private long date;
    private String status;
    private double total;

    public Order(String customerName, String customerPhone, long date, String status, double total) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.date = date;
        this.status = status;
        this.total = total;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public OrderStatus getOrderStatus() {
        return OrderStatus.fromString(status);
    }
}
