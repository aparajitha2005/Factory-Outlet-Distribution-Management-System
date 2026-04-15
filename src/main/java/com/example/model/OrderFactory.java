package com.example.model;

public class OrderFactory {
    public static Order createOrder(int id) {
        return new Order(id);
    }
}