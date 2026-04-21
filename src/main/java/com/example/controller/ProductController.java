package com.example.controller;

import com.example.model.Product;
import com.example.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private final DashboardService dashboardService;

    public ProductController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/product")
    public String viewProduct(Model model) {
        model.addAttribute("products", dashboardService.getProducts());
        return "product";
    }

    @PostMapping("/product/updateStock")
    public String updateStock(@RequestParam int productId, @RequestParam int quantity) {
        Product product = dashboardService.getProductById(productId);
        if (product != null) {
            product.updateStock(quantity);
        }
        return "redirect:/product";
    }
}