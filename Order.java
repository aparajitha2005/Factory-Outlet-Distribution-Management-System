package model;

public class Order {

    private int orderId;
    private String status;

    public Order(int orderId) {
        this.orderId = orderId;
        this.status = "Created";
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}