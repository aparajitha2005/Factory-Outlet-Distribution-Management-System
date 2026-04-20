package com.factoryoutlet.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    
    @Column(nullable = false, unique = true)
    private String productCode;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Integer stockQuantity = 0;
    
    @Column(nullable = false)
    private Integer reorderLevel = 10;  // Alert when stock falls below this
    
    @Column(nullable = false)
    private String category;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Update stock when receiving goods
    public void addStock(Integer quantity) {
        if (quantity > 0) {
            this.stockQuantity += quantity;
        }
    }
    
    // Update stock when dispatching shipment
    public void deductStock(Integer quantity) throws IllegalArgumentException {
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + stockQuantity + ", Requested: " + quantity);
        }
        this.stockQuantity -= quantity;
    }
    
    // Check if stock is sufficient for an order
    public boolean hasEnoughStock(Integer quantity) {
        return this.stockQuantity >= quantity;
    }
    
    // Check if stock is running low
    public boolean isLowStock() {
        return this.stockQuantity <= reorderLevel;
    }
}
