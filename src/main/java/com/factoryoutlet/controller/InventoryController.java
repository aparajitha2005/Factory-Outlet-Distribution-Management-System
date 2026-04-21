package com.factoryoutlet.controller;

import com.factoryoutlet.model.Product;
import com.factoryoutlet.model.Shipment;
import com.factoryoutlet.model.Warehouse;
import com.factoryoutlet.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * Receive stock from factory
     * POST /api/inventory/receive
     */
    @PostMapping("/receive")
    public ResponseEntity<Map<String, Object>> receiveStock(
            @RequestParam Long productId,
            @RequestParam Long warehouseId,
            @RequestParam Integer quantity) {
        try {
            Product product = inventoryService.receiveStock(productId, warehouseId, quantity);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Stock received successfully");
            response.put("productId", product.getProductId());
            response.put("productName", product.getProductName());
            response.put("newStockQuantity", product.getStockQuantity());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * Dispatch shipment (deduct stock)
     * POST /api/inventory/dispatch
     */
    @PostMapping("/dispatch")
    public ResponseEntity<Map<String, Object>> dispatchShipment(
            @RequestParam Long productId,
            @RequestParam Long warehouseId,
            @RequestParam Integer quantity,
            @RequestParam String destinationAddress) {
        try {
            Shipment shipment = inventoryService.dispatchShipment(productId, warehouseId, quantity, destinationAddress);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Shipment dispatched successfully");
            response.put("shipmentId", shipment.getShipmentId());
            response.put("status", shipment.getStatus());
            response.put("quantityShipped", shipment.getQuantityShipped());
            response.put("shippedAt", shipment.getShippedAt());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * Validate stock before placing order
     * GET /api/inventory/validate?productId=X&quantity=Y
     * Called by P1 (Order Management)
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateStock(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        try {
            boolean hasStock = inventoryService.validateSufficientStock(productId, quantity);
            
            Map<String, Object> response = new HashMap<>();
            response.put("productId", productId);
            response.put("requestedQuantity", quantity);
            response.put("isAvailable", hasStock);
            response.put("canPlaceOrder", hasStock);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * Check stock status (IN_STOCK, LOW_STOCK, OUT_OF_STOCK)
     * GET /api/inventory/status/{productId}
     */
    @GetMapping("/status/{productId}")
    public ResponseEntity<Map<String, Object>> checkStockStatus(@PathVariable Long productId) {
        try {
            InventoryService.StockStatus status = inventoryService.checkStockLevel(productId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("productId", productId);
            response.put("status", status.toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * Get all low stock products (alert/reorder list)
     * GET /api/inventory/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockProducts() {
        try {
            List<Product> lowStockProducts = inventoryService.getLowStockProducts();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", lowStockProducts.size());
            response.put("products", lowStockProducts);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * Get warehouse details and current stock
     * GET /api/inventory/warehouse/{warehouseId}
     */
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Map<String, Object>> getWarehouse(@PathVariable Long warehouseId) {
        try {
            Warehouse warehouse = inventoryService.getWarehouse(warehouseId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("warehouseId", warehouse.getWarehouseId());
            response.put("warehouseName", warehouse.getWarehouseName());
            response.put("location", warehouse.getLocation());
            response.put("capacity", warehouse.getCapacity());
            response.put("currentStock", warehouse.getCurrentStock());
            response.put("availableCapacity", warehouse.getCapacity() - warehouse.getCurrentStock());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * Update shipment status
     * PUT /api/inventory/shipment/{shipmentId}/status
     */
    @PutMapping("/shipment/{shipmentId}/status")
    public ResponseEntity<Map<String, Object>> updateShipmentStatus(
            @PathVariable Long shipmentId,
            @RequestParam String status) {
        try {
            Shipment.ShipmentStatus shipmentStatus = Shipment.ShipmentStatus.valueOf(status.toUpperCase());
            Shipment shipment = inventoryService.updateShipmentStatus(shipmentId, shipmentStatus);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("shipmentId", shipment.getShipmentId());
            response.put("status", shipment.getStatus().toString());
            response.put("updatedAt", shipment.getUpdatedAt());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
    
    /**
     * Get all shipments for a warehouse
     * GET /api/inventory/warehouse/{warehouseId}/shipments
     */
    @GetMapping("/warehouse/{warehouseId}/shipments")
    public ResponseEntity<Map<String, Object>> getWarehouseShipments(@PathVariable Long warehouseId) {
        try {
            List<Shipment> shipments = inventoryService.getWarehouseShipments(warehouseId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("warehouseId", warehouseId);
            response.put("shipmentCount", shipments.size());
            response.put("shipments", shipments);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}
