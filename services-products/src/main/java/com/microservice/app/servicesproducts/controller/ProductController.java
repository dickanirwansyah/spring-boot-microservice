package com.microservice.app.servicesproducts.controller;

import com.microservice.app.servicesproducts.entity.Product;
import com.microservice.app.servicesproducts.exception.ProductNotFoundExceptions;
import com.microservice.app.servicesproducts.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<Product> allProducts(HttpServletRequest request){
        log.info("Finding all products");
        /**
         * auth header security
         */
        String auth_header = request.getHeader("AUTH_HEADER");
        log.info("AUTH_HEADER: "+auth_header);
        return productService.findAllProducts();
    }

    @GetMapping(value = "/{code}")
    public Product productByCode(@PathVariable String code){
        log.info("Finding product by code: "+code);
        return productService.findProductByCode(code)
                .orElseThrow(()-> new ProductNotFoundExceptions("Product with code ["+code+"] doesnt exists"));
    }
}
