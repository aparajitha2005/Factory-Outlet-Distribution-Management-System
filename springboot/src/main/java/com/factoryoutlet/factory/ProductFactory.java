package com.factoryoutlet.factory;

import com.factoryoutlet.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductFactory {

    private static int productCounter = 1;

    public Product createProduct(String type) {
        switch (type.toLowerCase()) {

            case "electronics":
                return new Product(productCounter++, "Electronics Item", 25000, 50);

            case "clothing":
                return new Product(productCounter++, "Clothing Item", 1500, 200);

            case "appliance":
                return new Product(productCounter++, "Home Appliance", 12000, 30);

            default:
                return new Product(productCounter++, "General Product", 5000, 100);
        }
    }
}