package com.factoryoutlet.repository;

import com.factoryoutlet.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByWarehouseName(String warehouseName);
    Optional<Warehouse> findByLocation(String location);
}
