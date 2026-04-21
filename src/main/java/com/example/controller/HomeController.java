package com.example.controller;

import com.example.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalProducts", dashboardService.getTotalProducts());
        model.addAttribute("activeOrders", dashboardService.getActiveOrders());
        return "index";
    }
}