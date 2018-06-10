package com.springcloud.app.inventoryservice.repository;

import com.springcloud.app.inventoryservice.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long>{

    Optional<InventoryItem> findByProductCode(String productCode);
}
