package com.software.modsen.ridesmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@EnableWebMvc
@EnableFeignClients
public class RidesMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RidesMicroserviceApplication.class, args);
    }

}
