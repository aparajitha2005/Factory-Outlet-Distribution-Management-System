package com.factoryoutlet.service;

import com.factoryoutlet.model.Product;
import com.factoryoutlet.model.Shipment;
import com.factoryoutlet.model.Warehouse;
import com.factoryoutlet.repository.ProductRepository;
import com.factoryoutlet.repository.ShipmentRepository;
import com.factoryoutlet.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private ShipmentRepository shipmentRepository;
    
    /**
     * Receive stock into warehouse from factory
     * Called by P2 (Factory/Product module)
     */
    public Product receiveStock(Long productId, Long warehouseId, Integer quantity) throws Exception {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        // Get product
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new Exception("Product not found with ID: " + productId));
        
        // Get warehouse
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new Exception("Warehouse not found with ID: " + warehouseId));
        
        // Check warehouse capacity
        if (!warehouse.hasCapacity(quantity)) {
            throw new Exception("Warehouse " + warehouse.getWarehouseName() + " does not have capacity for " + quantity + " units");
        }
        
        // Update stock
        product.addStock(quantity);
        warehouse.setCurrentStock(warehouse.getCurrentStock() + quantity);
        
        // Save
        productRepository.save(product);
        warehouseRepository.save(warehouse);
        
        return product;
    }
    
    /**
     * Dispatch shipment from warehouse to customer
     * Called by P1 (Order Management) after order is confirmed
     */
    public Shipment dispatchShipment(Long productId, Long warehouseId, Integer quantity, String destinationAddress) throws Exception {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        // Validate stock exists
        if (!validateSufficientStock(productId, quantity)) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));
            throw new Exception("Insufficient stock for product: " + product.getProductName() + 
                              ". Available: " + product.getStockQuantity() + ", Requested: " + quantity);
        }
        
        // Get product and warehouse
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new Exception("Product not found with ID: " + productId));
        
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new Exception("Warehouse not found with ID: " + warehouseId));
        
        // Deduct stock
        product.deductStock(quantity);
        warehouse.setCurrentStock(warehouse.getCurrentStock() - quantity);
        
        // Create shipment record
        Shipment shipment = new Shipment();
        shipment.setWarehouse(warehouse);
        shipment.setProduct(product);
        shipment.setQuantityShipped(quantity);
        shipment.setDestinationAddress(destinationAddress);
        shipment.setStatus(Shipment.ShipmentStatus.PENDING);
        shipment.setShippedAt(LocalDateTime.now());
        
        // Save
        productRepository.save(product);
        warehouseRepository.save(warehouse);
        shipmentRepository.save(shipment);
        
        return shipment;
    }
    
    /**
     * Validate if sufficient stock exists for an order
     * Called by P1 BEFORE placing order
     */
    public boolean validateSufficientStock(Long productId, Integer quantity) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            return false;
        }
        return product.get().hasEnoughStock(quantity);
    }
    
    /**
     * Check stock level status for a product
     */
    public StockStatus checkStockLevel(Long productId) throws Exception {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new Exception("Product not found with ID: " + productId));
        
        if (product.getStockQuantity() == 0) {
            return StockStatus.OUT_OF_STOCK;
        } else if (product.isLowStock()) {
            return StockStatus.LOW_STOCK;
        } else {
            return StockStatus.IN_STOCK;
        }
    }
    
    /**
     * Get all products with low stock (for alerts)
     */
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }
    
    /**
     * Get all shipments for a warehouse
     */
    public List<Shipment> getWarehouseShipments(Long warehouseId) {
        return shipmentRepository.findByWarehouse_WarehouseId(warehouseId);
    }
    
    /**
     * Update shipment status (when delivered, etc)
     */
    public Shipment updateShipmentStatus(Long shipmentId, Shipment.ShipmentStatus status) throws Exception {
        Shipment shipment = shipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new Exception("Shipment not found with ID: " + shipmentId));
        
        shipment.setStatus(status);
        if (status == Shipment.ShipmentStatus.DELIVERED) {
            shipment.setDeliveredAt(LocalDateTime.now());
        }
        
        return shipmentRepository.save(shipment);
    }
    
    /**
     * Get warehouse details
     */
    public Warehouse getWarehouse(Long warehouseId) throws Exception {
        return warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new Exception("Warehouse not found with ID: " + warehouseId));
    }
    
    public enum StockStatus {
        IN_STOCK, LOW_STOCK, OUT_OF_STOCK
    }
}
