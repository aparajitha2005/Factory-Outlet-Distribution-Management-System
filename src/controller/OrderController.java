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

        if (order == null) {
            view.showMessage("Order not initialized!");
            return;
        }

        view.showMessage("Order Created with ID: " + order.getOrderId());
    }

    public void updateStatus(String status) {

        if (order == null) {
            view.showMessage("No order created yet!");
            return;
        }

        String[] validStatuses = {
                "created", "confirmed", "processing",
                "packed", "shipped", "delivered", "cancelled"
        };

        boolean valid = false;

        for (String s : validStatuses) {
            if (s.equalsIgnoreCase(status)) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            view.showMessage("Invalid status!");
            return;
        }

        order.setStatus(status);
        view.showMessage("Order status updated to: " + status);
    }

    public void displayOrder() {

        if (order == null) {
            view.showMessage("No order available!");
            return;
        }

        view.displayOrder(order.getOrderId(), order.getStatus());
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}