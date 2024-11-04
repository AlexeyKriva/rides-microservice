package com.software.modsen.ridesmicroservice.clients;

import com.software.modsen.ridesmicroservice.configs.FeignConfig;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccount;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccountBalanceDownDto;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccountBalanceUpDto;
import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "driver-microservice", configuration = FeignConfig.class)
public interface DriverClient {
    @GetMapping("/{id}")
    ResponseEntity<Driver> getDriverById(
            @PathVariable("id") long id
    );

    @PutMapping("/{driver_id}/accounts/down")
    ResponseEntity<DriverAccount> cancelBalanceByPassengerId(
            @PathVariable("driver_id") long driverId,
            @Valid @RequestBody DriverAccountBalanceDownDto driverAccountBalanceDownDto);

    @PutMapping("/{driver_id}/accounts/up")
    ResponseEntity<DriverAccount> increaseBalanceByDriverId(
            @PathVariable("driver_id") long driverId,
            @Valid @RequestBody DriverAccountBalanceUpDto driverAccountBalanceUpDto);
}