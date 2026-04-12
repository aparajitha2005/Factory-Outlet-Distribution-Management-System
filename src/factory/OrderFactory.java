package factory;

import model.Order;

public class OrderFactory {

    public static Order createOrder(int orderId) {
        // Default order creation
        Order order = new Order(orderId);
        order.setStatus("Created");
        return order;
    }
}