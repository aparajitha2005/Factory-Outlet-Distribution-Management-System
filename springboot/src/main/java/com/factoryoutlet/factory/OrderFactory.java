package com.factoryoutlet.factory;

import com.factoryoutlet.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {

    private static int orderCounter = 1;

    public Order createOrder(String type) {
        switch (type.toLowerCase()) {
            case "bulk":
                Order bulk = new Order(orderCounter++, "Bulk");
                bulk.setStatus("Bulk Order Created");
                return bulk;
            case "priority":
                Order priority = new Order(orderCounter++, "Priority");
                priority.setStatus("Priority Order Created");
                return priority;
            default:
                return new Order(orderCounter++, "Normal");
        }
    }
}
