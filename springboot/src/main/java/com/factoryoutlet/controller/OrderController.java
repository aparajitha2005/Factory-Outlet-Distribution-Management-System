package com.factoryoutlet.controller;

import com.factoryoutlet.model.Order;
import com.factoryoutlet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public String placeOrder(@RequestParam String type, @RequestParam int quantity) {
        Order order = orderService.placeOrder(type, quantity);
        if (order == null) {
            return "Insufficient stock!";
        }
        return "Order placed! ID: " + order.getOrderId() + " | Type: " + order.getType() + " | Status: " + order.getStatus();
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
