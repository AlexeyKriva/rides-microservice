package com.software.modsen.ridesmicroservice.clients;

import com.software.modsen.ridesmicroservice.entities.account.DriverAccount;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccountBalanceDownDto;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccountBalanceUpDto;
import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "driver-microservice")
public interface DriverClient {
    @GetMapping("/{id}")
    @Transactional
    ResponseEntity<Driver> getDriverById(@PathVariable("id") long id);

    @PutMapping("/account/{driver_id}/cancel")
    ResponseEntity<DriverAccount> cancelBalanceByPassengerId(
            @PathVariable("driver_id") long driverId,
            @Valid @RequestBody DriverAccountBalanceDownDto driverAccountBalanceDownDto);

    @PutMapping("/account/{driver_id}/increase")
    @Transactional
    ResponseEntity<DriverAccount> increaseBalanceByDriverId(
            @PathVariable("driver_id") long driverId,
            @Valid @RequestBody DriverAccountBalanceUpDto driverAccountBalanceUpDto);
}