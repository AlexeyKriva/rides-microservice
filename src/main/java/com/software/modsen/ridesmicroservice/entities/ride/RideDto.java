package com.software.modsen.ridesmicroservice.entities.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.modsen.ridesmicroservice.entities.Driver.Driver;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RideDto {
    @JsonProperty("passenger_id")
    private long passengerId;
    @JsonProperty("driver_id")
    private long driverId;
    @JsonProperty("from_address")
    private String fromAddress;
    @JsonProperty("to_address")
    private String toAddress;
    @JsonProperty("ride_status")
    private RideStatus rideStatus;
    @JsonProperty("order_date_time")
    private LocalDateTime orderDateTime;
    @JsonProperty("price")
    private float price;
}
