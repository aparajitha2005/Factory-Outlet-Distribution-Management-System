package com.factoryoutlet.service;

import com.factoryoutlet.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private Product product;

    public Product getProduct() {
        return product;
    }

    public String updateStock(int qty) {
        if (qty <= 0) {
            return "Invalid quantity!";
        }
        product.setStock(product.getStock() + qty);
        return "Stock updated successfully! New stock: " + product.getStock();
    }
}
