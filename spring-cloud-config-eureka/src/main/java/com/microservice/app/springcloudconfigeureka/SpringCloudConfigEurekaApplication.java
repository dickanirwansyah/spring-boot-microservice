package com.microservice.app.springcloudconfigeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SpringCloudConfigEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudConfigEurekaApplication.class, args);
	}
}
