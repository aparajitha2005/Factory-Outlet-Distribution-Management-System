package com.example.controller;

import com.example.model.Order;
import com.example.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    private final DashboardService dashboardService;

    public OrderController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/order")
    public String viewOrders(Model model) {
        model.addAttribute("orders", dashboardService.getOrders());
        model.addAttribute("nextOrderId", dashboardService.getNextOrderId());
        model.addAttribute("products", dashboardService.getProducts());
        return "order";
    }

    @PostMapping("/order/create")
    public String createOrder(@RequestParam int productId, @RequestParam int quantity, Model model) {
        Order created = dashboardService.createNewOrder(productId, quantity);
        if (created == null) {
            model.addAttribute("orders", dashboardService.getOrders());
            model.addAttribute("nextOrderId", dashboardService.getNextOrderId());
            model.addAttribute("products", dashboardService.getProducts());
            model.addAttribute("error", "Insufficient stock or invalid product.");
            return "order";
        }
        return "redirect:/order";
    }

    @PostMapping("/order/updateStatus")
    public String updateOrderStatus(@RequestParam int orderId, @RequestParam String status) {
        Order order = dashboardService.getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
        }
        return "redirect:/order";
    }

    @PostMapping("/order/setPending")
    public String setOrderToPending(@RequestParam int orderId) {
        Order order = dashboardService.getOrderById(orderId);
        if (order != null) {
            order.setStatus("Pending");
        }
        return "redirect:/order";
    }
}