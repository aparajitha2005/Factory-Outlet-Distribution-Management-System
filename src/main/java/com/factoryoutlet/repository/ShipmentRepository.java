package com.factoryoutlet.repository;

import com.factoryoutlet.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    List<Shipment> findByWarehouse_WarehouseId(Long warehouseId);
    List<Shipment> findByProduct_ProductId(Long productId);
    List<Shipment> findByStatus(Shipment.ShipmentStatus status);
    
    @Query("SELECT s FROM Shipment s WHERE s.warehouse.warehouseId = :warehouseId AND s.status = :status")
    List<Shipment> findByWarehouseAndStatus(@Param("warehouseId") Long warehouseId, @Param("status") Shipment.ShipmentStatus status);
}
