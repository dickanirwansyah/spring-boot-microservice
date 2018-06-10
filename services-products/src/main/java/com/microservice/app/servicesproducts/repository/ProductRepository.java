package com.microservice.app.servicesproducts.repository;

import com.microservice.app.servicesproducts.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{

    //cari product berdasarkan code string
    Optional<Product> findByCode(String code);
}
