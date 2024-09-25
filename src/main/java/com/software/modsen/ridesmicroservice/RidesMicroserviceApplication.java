package com.software.modsen.ridesmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableFeignClients
@EnableDiscoveryClient
public class RidesMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RidesMicroserviceApplication.class, args);
    }

}