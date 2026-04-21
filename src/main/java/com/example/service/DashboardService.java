package com.example.service;

import com.example.model.Order;
import com.example.model.OrderFactory;
import com.example.model.Product;
import com.example.model.ProductFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DashboardService {

    private final List<Product> products;
    private final List<Order> orders;
    private int nextOrderId = 101;

    public DashboardService() {
        this.products = new ArrayList<>();
        this.products.add(ProductFactory.createProduct(1, "Laptop", 50000, 10));
        this.products.add(ProductFactory.createProduct(2, "Smartphone", 25000, 15));
        this.products.add(ProductFactory.createProduct(3, "Headphones", 3000, 40));
        this.products.add(ProductFactory.createProduct(4, "Keyboard", 1500, 25));
        this.products.add(ProductFactory.createProduct(5, "Mouse", 1200, 30));

        this.orders = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Product getProductById(int id) {
        return products.stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Product getPrimaryProduct() {
        return products.isEmpty() ? null : products.get(0);
    }

    public int getTotalProducts() {
        return products.size();
    }

    public int getActiveOrders() {
        return (int) orders.stream()
                .filter(order -> !"Pending".equalsIgnoreCase(order.getStatus()) && !"Delivered".equalsIgnoreCase(order.getStatus()))
                .count();
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public Order getOrderById(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public int getNextOrderId() {
        return nextOrderId;
    }

    public Order createNewOrder(int productId, int quantity) {
        Product product = getProductById(productId);
        if (product == null) return null;
        if (product.getStock() < quantity) return null;
        product.setStock(product.getStock() - quantity);
        Order newOrder = new Order(nextOrderId++, productId, product.getName(), quantity);
        orders.add(newOrder);
        return newOrder;
    }

    @Deprecated
    public Order getOrder() {
        return orders.isEmpty() ? null : orders.get(orders.size() - 1);
    }
}