package com.factoryoutlet.model;

import org.springframework.stereotype.Component;

@Component
public class Product {

    private int id;
    private String name;
    private double price;
    private int stock;

    // existing constructor (already there)
    public Product() {
        this.id = 1;
        this.name = "Laptop";
        this.price = 50000;
        this.stock = 10;
    }

    // ADD THIS ONE NEW CONSTRUCTOR — nothing else touched
    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}