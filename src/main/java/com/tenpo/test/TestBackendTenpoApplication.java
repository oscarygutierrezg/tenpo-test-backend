package com.tenpo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
@ComponentScan("com.tenpo.test")
public class TestBackendTenpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestBackendTenpoApplication.class, args);
	}
}
