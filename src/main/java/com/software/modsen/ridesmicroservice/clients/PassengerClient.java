package com.software.modsen.ridesmicroservice.clients;

import com.software.modsen.ridesmicroservice.entities.account.PassengerAccount;
import com.software.modsen.ridesmicroservice.entities.account.PassengerAccountCancelDto;
import com.software.modsen.ridesmicroservice.entities.account.PassengerAccountIncreaseDto;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "passenger-microservice")
public interface PassengerClient {
    @GetMapping("/{id}")
    ResponseEntity<Passenger> getPassengerById(@PathVariable("id") long id);

    @PutMapping("/{passenger_id}/increase")
    public ResponseEntity<PassengerAccount> increaseBalanceByPassengerId(
            @PathVariable("passenger_id") long passengerId,
            @Valid @RequestBody PassengerAccountIncreaseDto passengerAccountIncreaseDto);

    @PutMapping("/account/{passenger_id}/cancel")
    ResponseEntity<PassengerAccount> cancelBalanceByPassengerId(
            @PathVariable("passenger_id") long passengerId,
            @Valid @RequestBody PassengerAccountCancelDto passengerAccountCancelDto);
}