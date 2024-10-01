package com.software.modsen.ridesmicroservice.entities.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class RidePatchDto {
    @JsonProperty("passenger_id")
    private Long passengerId;

    @JsonProperty("driver_id")
    private Long driverId;

    @JsonProperty("from_address")
    @Schema(example = "Nezavisimosty 7")
    private String fromAddress;

    @JsonProperty("to_address")
    @Schema(example = "Nezavisimosty 183")
    private String toAddress;

    @JsonProperty("ride_status")
    private RideStatus rideStatus;

    @PastOrPresent(message = "Date cannot be in the future.")
    @JsonProperty("order_date_time")
    @Schema(example = "2024-15-03T12:00:00")
    private LocalDateTime orderDateTime;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("currency")
    private Currency currency;
}
