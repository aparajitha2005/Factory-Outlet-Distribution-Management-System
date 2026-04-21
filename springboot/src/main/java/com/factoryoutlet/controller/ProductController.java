package com.factoryoutlet.controller;

import com.factoryoutlet.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private Product product;

    @GetMapping("/info")
    public Product getProduct() {
        return product;
    }

    @PutMapping("/update-stock")
    public String updateStock(@RequestParam int stock) {
        product.setStock(stock);
        return "Stock updated to: " + stock;
    }
}
