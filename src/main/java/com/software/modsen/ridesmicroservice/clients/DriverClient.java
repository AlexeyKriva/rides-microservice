package com.software.modsen.ridesmicroservice.clients;

import com.software.modsen.ridesmicroservice.entities.Driver.Driver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver-microservice", url = "http://localhost:8082/api/driver")
public interface DriverClient {
    @GetMapping("/{id}")
    ResponseEntity<Driver> getDriverById(@PathVariable("id") long id);
}
