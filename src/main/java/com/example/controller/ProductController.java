package com.example.controller;

import com.example.model.Product;
import com.example.model.ProductFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private Product product = ProductFactory.createProduct(1, "Laptop", 50000, 10);

    @GetMapping("/product")
    public String viewProduct(Model model) {
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping("/product/updateStock")
    public String updateStock(@RequestParam int quantity) {
        product.updateStock(quantity);
        return "redirect:/product";
    }
}