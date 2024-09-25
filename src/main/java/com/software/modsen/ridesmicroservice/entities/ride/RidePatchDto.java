package com.software.modsen.ridesmicroservice.entities.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RidePatchDto {
    @JsonProperty("passenger_id")
    private Long passengerId;

    @JsonProperty("driver_id")
    private Long driverId;

    @JsonProperty("from_address")
    private String fromAddress;

    @JsonProperty("to_address")
    private String toAddress;

    @JsonProperty("ride_status")
    private RideStatus rideStatus;

    @PastOrPresent(message = "Date cannot be in the future.")
    @JsonProperty("order_date_time")
    private LocalDateTime orderDateTime;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("currency")
    private Currency currency;
}
