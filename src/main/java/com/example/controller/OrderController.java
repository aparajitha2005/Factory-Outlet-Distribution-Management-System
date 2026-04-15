package com.example.controller;

import com.example.model.Order;
import com.example.model.OrderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    private Order order = OrderFactory.createOrder(101);

    @GetMapping("/order")
    public String viewOrder(Model model) {
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping("/order/create")
    public String createOrder() {
        order.createOrder();
        return "redirect:/order";
    }

    @PostMapping("/order/updateStatus")
    public String updateOrderStatus(@RequestParam String status) {
        order.setStatus(status);
        return "redirect:/order";
    }
}