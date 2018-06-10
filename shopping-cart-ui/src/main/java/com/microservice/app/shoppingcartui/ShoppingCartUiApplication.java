package com.microservice.app.shoppingcartui;

import com.microservice.app.shoppingcartui.filter.AuthHeaderFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ShoppingCartUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartUiApplication.class, args);
	}

	@Bean
	AuthHeaderFilter authHeaderFilter(){
		return new AuthHeaderFilter();
	}
}
