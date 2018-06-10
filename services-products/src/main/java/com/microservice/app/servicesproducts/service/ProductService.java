package com.microservice.app.servicesproducts.service;


import com.microservice.app.servicesproducts.entity.Product;
import com.microservice.app.servicesproducts.models.ProductInventoryResponse;
import com.microservice.app.servicesproducts.repository.ProductRepository;
import com.microservice.app.servicesproducts.utils.MyThreadLocalsHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductService {


    private final ProductRepository productRepository;

    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public ProductService(ProductRepository productRepository, InventoryServiceClient inventoryServiceClient){
        this.productRepository = productRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    /**
     * menampilkan semua data product
     * yang tersedia.
     */
    public List<Product> findAllProducts(){
        List<Product> products = productRepository.findAll();
        final Map<String, Integer> inventoryLevels = getInventoryLevelsWithFeignClient();
        final List<Product> availableProduct = products.stream()
                .filter(p -> inventoryLevels.get(p.getCode()) != null
                        && inventoryLevels.get(p.getCode()) > 0)
                .collect(Collectors.toList());
        return availableProduct;
    }

    private Map<String, Integer> getInventoryLevelsWithFeignClient(){
        log.info("Fetching inventory levels using FeignClient");
        Map<String, Integer> inventoryLevels = new HashMap<>();
        List<ProductInventoryResponse> inventory = inventoryServiceClient.getProductInventoryLevels();
        for(ProductInventoryResponse response : inventory){
            inventoryLevels.put(response.getProductCode(), response.getAvailableQuantity());
        }
        log.debug("InventoryLevels : {}", inventoryLevels);
        return inventoryLevels;
    }

    /**
     * @param code
     * menampilkan product berdasarkan code yang memiliki quantiy / jumlah > 0
     *
     */
    public Optional<Product> findProductByCode(String code){
        Optional<Product> productOptional = productRepository.findByCode(code);
        if(productOptional.isPresent()){
            String correlationId = UUID.randomUUID().toString();
            MyThreadLocalsHolder.setCorrelationId(correlationId);
            log.info("Before CorrelationID: "+MyThreadLocalsHolder.getCorrelationId());
            log.info("Fetching inventory level for product_code : "+code);

            Optional<ProductInventoryResponse> itemsResponseEntity =
                    this.inventoryServiceClient.getProductInventoryByCode(code);
            if (itemsResponseEntity.isPresent()){
                Integer quantity = itemsResponseEntity.get().getAvailableQuantity();
                productOptional.get().setInStock(quantity > 0);
            }
            log.info("After CorrelationID: "+MyThreadLocalsHolder.getCorrelationId());
        }
        return productOptional;
    }
}
