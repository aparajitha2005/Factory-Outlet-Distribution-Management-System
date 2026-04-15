package com.example.model;

public class ProductFactory {
    public static Product createProduct(int id, String name, double price, int stock) {
        return new Product(id, name, price, stock);
    }

    // For different types, but for now, simple
}