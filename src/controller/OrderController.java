package controller;

import model.Order;
import view.OrderView;

public class OrderController {

    private Order order;
    private OrderView view;

    public OrderController(Order order, OrderView view) {
        this.order = order;
        this.view = view;
    }

    public void createOrder() {
        view.showMessage("Order Created with ID: " + order.getOrderId());
    }

    public void updateStatus(String status) {
        order.setStatus(status);
        view.showMessage("Order status updated to: " + status);
    }

    public void displayOrder() {
        view.displayOrder(order.getOrderId(), order.getStatus());
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}