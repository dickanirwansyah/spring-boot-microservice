package com.microservice.app.servicesproducts.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundExceptions extends RuntimeException{

    public ProductNotFoundExceptions(){}

    public ProductNotFoundExceptions(String message){
        super(message);
    }

    public ProductNotFoundExceptions(String message, Throwable cause){
        super(message, cause);
    }

    public ProductNotFoundExceptions(Throwable cause){
        super(cause);
    }

}
