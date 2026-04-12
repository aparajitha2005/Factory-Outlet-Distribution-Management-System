package factory;

import model.Order;

public class OrderFactory {

    public static Order createOrder(String type, int orderId) {

        Order order = new Order(orderId);

        switch (type.toLowerCase()) {

            case "bulk":
                order.setStatus("Bulk Order Created");
                break;

            case "priority":
                order.setStatus("Priority Order Created");
                break;

            default:
                order.setStatus("Normal Order Created");
        }

        return order;
    }
}