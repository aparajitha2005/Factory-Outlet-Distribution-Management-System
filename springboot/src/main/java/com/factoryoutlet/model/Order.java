package com.factoryoutlet.model;

public class Order {

    private int orderId;
    private String status;
    private String type;

    public Order(int orderId, String type) {
        this.orderId = orderId;
        this.type = type;
        this.status = "Created";
    }

    public int getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getType() { return type; }

    public void setStatus(String status) { this.status = status; }
    public void setType(String type) { this.type = type; }
}
