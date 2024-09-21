package com.software.modsen.ridesmicroservice.entities.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RidePutDto {
    @NotNull(message = "Passenger id cannot be null.")
    @JsonProperty("passenger_id")
    private Long passengerId;
    @NotNull(message = "Driver id cannot be null.")
    @JsonProperty("driver_id")
    private Long driverId;
    @NotBlank(message = "Departure address cannot be blank.")
    @JsonProperty("from_address")
    private String fromAddress;
    @NotBlank(message = "Arrival address cannot be blank.")
    @JsonProperty("to_address")
    private String toAddress;
    @NotNull(message = "Ride status cannot be null.")
    @JsonProperty("ride_status")
    private RideStatus rideStatus;
    @PastOrPresent(message = "Date cannot be in the future.")
    @NotNull(message = "Order date time cannot be null.")
    @JsonProperty("order_date_time")
    private LocalDateTime orderDateTime;
    @NotNull(message = "Price cannot be null.")
    @JsonProperty("price")
    private Float price;
    @NotNull(message = "Currency cannot be null.")
    @JsonProperty("currency")
    private Currency currency;
}
