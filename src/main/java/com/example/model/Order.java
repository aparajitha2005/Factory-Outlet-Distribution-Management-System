package com.example.model;

public class Order {
    private int id;
    private String status;
    private String productName;
    private int productId;
    private int quantity;

    public Order(int id) {
        this.id = id;
        this.status = "Pending";
    }

    public Order(int id, int productId, String productName, int quantity) {
        this.id = id;
        this.status = "Processing";
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getProductName() { return productName; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }

    public void createOrder() {
        if ("Pending".equalsIgnoreCase(this.status)) {
            this.status = "Processing";
            System.out.println("Order created with ID: " + id);
        }
    }

    public void updateOrderStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Order status updated to: " + status);
    }

    public void displayOrder() {
        System.out.println("Order ID: " + id + ", Status: " + status);
    }
}