package com.factoryoutlet.service;

import com.factoryoutlet.factory.OrderFactory;
import com.factoryoutlet.model.Order;
import com.factoryoutlet.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderFactory orderFactory;

    @Autowired
    private Product product;

    private List<Order> orders = new ArrayList<>();

    public Order placeOrder(String type, int quantity) {
        if (product.getStock() < quantity) {
            return null;
        }
        product.setStock(product.getStock() - quantity);
        Order order = orderFactory.createOrder(type);
        orders.add(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}
