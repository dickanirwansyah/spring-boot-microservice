package com.springcloud.app.inventoryservice.controller;


import com.springcloud.app.inventoryservice.entity.InventoryItem;
import com.springcloud.app.inventoryservice.repository.InventoryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class ControllerInventoryItem {


    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public ControllerInventoryItem(InventoryItemRepository inventoryItemRepository){
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @GetMapping(value = "/api/inventory/{productCode}")
    public ResponseEntity<Optional<InventoryItem>> findInventoryByProductCode(@PathVariable String productCode){
        log.info("finding inventory code {} :"+productCode);
        Optional<InventoryItem> inventoryItem = inventoryItemRepository.findByProductCode(productCode);
        if (!inventoryItem.isPresent()){
            return new ResponseEntity<Optional<InventoryItem>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Optional<InventoryItem>>(inventoryItem, HttpStatus.OK);
    }

    @GetMapping(value = "/api/inventory")
    public List<InventoryItem> getInventory(){
        log.info("finding inventory all");
        return inventoryItemRepository.findAll();
    }
}
