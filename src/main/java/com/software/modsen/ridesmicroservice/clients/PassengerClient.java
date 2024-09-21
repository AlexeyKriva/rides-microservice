package com.software.modsen.ridesmicroservice.clients;

import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.swing.text.html.parser.Entity;

@FeignClient(name = "passenger-microservice", url = "http://localhost:8081/api/passenger")
public interface PassengerClient {
    @GetMapping("/{id}")
    ResponseEntity<Passenger> getPassengerById(@PathVariable("id") long id);
}
