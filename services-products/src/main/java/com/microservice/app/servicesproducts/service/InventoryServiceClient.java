package com.microservice.app.servicesproducts.service;


import com.microservice.app.servicesproducts.models.ProductInventoryResponse;
import com.microservice.app.servicesproducts.utils.MyThreadLocalsHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InventoryServiceFeignClient inventoryServiceFeignClient;

    private static final String INVENTORY_API_PATH="http://inventory-service/api/";

    @HystrixCommand(fallbackMethod = "getProductInventoryLevels",
        commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
        })
    public List<ProductInventoryResponse> getProductInventoryLevels(){
        return this.inventoryServiceFeignClient.getInventoryLevels();
    }

    @SuppressWarnings("unused")
    List<ProductInventoryResponse> getDefaultProductInventoryLevels(){
        log.info("Returning default product inventory levels");
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "getProductInventoryByCode")
    public Optional<ProductInventoryResponse> getProductInventoryByCode(String productCode){
        log.info("CorrelationID : "+ MyThreadLocalsHolder.getCorrelationId());
        ResponseEntity<ProductInventoryResponse> itemsResponseEntity =
                restTemplate.getForEntity(INVENTORY_API_PATH + "inventory/{code}",
                        ProductInventoryResponse.class, productCode);


        /**
         * jika response 200 ok dan error
         */
        if (itemsResponseEntity.getStatusCode() == HttpStatus.OK){
            Integer quantity = itemsResponseEntity.getBody().getAvailableQuantity();
            log.info("Available quantity product : "+quantity);
            return Optional.ofNullable(itemsResponseEntity.getBody());
        }else{
            log.error("Unable to get inventory level for product_code: "+productCode+" StatusCode: "+itemsResponseEntity.getStatusCode());
            return Optional.empty();
        }
    }

    @SuppressWarnings("unused")
    Optional<ProductInventoryResponse> getDefaultProductInventoryByCode(String productCode){
        log.info("Returning default ProductInventoryByCode for productCode : "+productCode);
        log.info("CorrelationID : "+MyThreadLocalsHolder.getCorrelationId());
        ProductInventoryResponse response = new ProductInventoryResponse();
        response.setProductCode(productCode);
        response.setAvailableQuantity(50);
        return Optional.ofNullable(response);
    }
}
