package com.microservice.app.servicesproducts.service;


import com.microservice.app.servicesproducts.models.ProductInventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface InventoryServiceFeignClient {

    @GetMapping(value = "/api/inventory")
    List<ProductInventoryResponse> getInventoryLevels();

    @GetMapping(value = "/api/inventory/{productCode}")
    List<ProductInventoryResponse> getInventoryByProductCode(String productCode);
}
