package com.example.model;

public class Order {
    private int id;
    private String status;

    public Order(int id) {
        this.id = id;
        this.status = "Pending";
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void createOrder() {
        // Logic to create order, perhaps add products
        System.out.println("Order created with ID: " + id);
    }

    public void updateOrderStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Order status updated to: " + status);
    }

    public void displayOrder() {
        System.out.println("Order ID: " + id + ", Status: " + status);
    }
}