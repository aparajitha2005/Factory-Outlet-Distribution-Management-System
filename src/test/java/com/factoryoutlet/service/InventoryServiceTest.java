package com.factoryoutlet.service;

import com.factoryoutlet.model.Product;
import com.factoryoutlet.model.Shipment;
import com.factoryoutlet.model.Warehouse;
import com.factoryoutlet.repository.ProductRepository;
import com.factoryoutlet.repository.ShipmentRepository;
import com.factoryoutlet.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private WarehouseRepository warehouseRepository;
    
    @Mock
    private ShipmentRepository shipmentRepository;
    
    @InjectMocks
    private InventoryService inventoryService;
    
    private Product testProduct;
    private Warehouse testWarehouse;
    
    @BeforeEach
    public void setUp() {
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductCode("PROD001");
        testProduct.setProductName("Test Product");
        testProduct.setPrice(100.0);
        testProduct.setStockQuantity(50);
        testProduct.setReorderLevel(10);
        testProduct.setCategory("Electronics");
        
        testWarehouse = new Warehouse();
        testWarehouse.setWarehouseId(1L);
        testWarehouse.setWarehouseName("Main Warehouse");
        testWarehouse.setLocation("Bangalore");
        testWarehouse.setCapacity(1000);
        testWarehouse.setCurrentStock(500);
    }
    
    @Test
    public void testReceiveStock_Success() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(testWarehouse);
        
        Product result = inventoryService.receiveStock(1L, 1L, 10);
        
        assertEquals(60, result.getStockQuantity());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
    }
    
    @Test
    public void testReceiveStock_ZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> 
            inventoryService.receiveStock(1L, 1L, 0)
        );
    }
    
    @Test
    public void testReceiveStock_ProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(Exception.class, () -> 
            inventoryService.receiveStock(999L, 1L, 10)
        );
    }
    
    @Test
    public void testReceiveStock_WarehouseCapacityExceeded() throws Exception {
        testWarehouse.setCurrentStock(995);  // Only 5 spaces left
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
        
        assertThrows(Exception.class, () -> 
            inventoryService.receiveStock(1L, 1L, 10)  // Trying to add 10, but only 5 spaces
        );
    }
    
    @Test
    public void testValidateSufficientStock_Enough() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        boolean result = inventoryService.validateSufficientStock(1L, 30);
        
        assertTrue(result);
    }
    
    @Test
    public void testValidateSufficientStock_NotEnough() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        boolean result = inventoryService.validateSufficientStock(1L, 100);
        
        assertFalse(result);
    }
    
    @Test
    public void testValidateSufficientStock_ProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        boolean result = inventoryService.validateSufficientStock(999L, 10);
        
        assertFalse(result);
    }
    
    @Test
    public void testCheckStockLevel_InStock() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        InventoryService.StockStatus status = inventoryService.checkStockLevel(1L);
        
        assertEquals(InventoryService.StockStatus.IN_STOCK, status);
    }
    
    @Test
    public void testCheckStockLevel_LowStock() throws Exception {
        testProduct.setStockQuantity(5);  // Below reorderLevel of 10
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        InventoryService.StockStatus status = inventoryService.checkStockLevel(1L);
        
        assertEquals(InventoryService.StockStatus.LOW_STOCK, status);
    }
    
    @Test
    public void testCheckStockLevel_OutOfStock() throws Exception {
        testProduct.setStockQuantity(0);
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        InventoryService.StockStatus status = inventoryService.checkStockLevel(1L);
        
        assertEquals(InventoryService.StockStatus.OUT_OF_STOCK, status);
    }
    
    @Test
    public void testDispatchShipment_Success() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(testWarehouse));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(testWarehouse);
        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Shipment result = inventoryService.dispatchShipment(1L, 1L, 10, "123 Main St");
        
        assertNotNull(result);
        assertEquals(10, result.getQuantityShipped());
        assertEquals(Shipment.ShipmentStatus.PENDING, result.getStatus());
        verify(shipmentRepository, times(1)).save(any(Shipment.class));
    }
    
    @Test
    public void testDispatchShipment_InsufficientStock() throws Exception {
        testProduct.setStockQuantity(5);
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        
        assertThrows(Exception.class, () -> 
            inventoryService.dispatchShipment(1L, 1L, 10, "123 Main St")
        );
    }
}
