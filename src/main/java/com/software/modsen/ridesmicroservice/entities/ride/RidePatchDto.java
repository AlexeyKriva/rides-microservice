package com.software.modsen.ridesmicroservice.entities.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@Schema(description = "Ride entity.")
public record RidePatchDto(
        @JsonProperty("passengerId")
        String passengerId,

        @JsonProperty("driverId")
        Long driverId,

        @JsonProperty("fromAddress")
        @Schema(example = "Nezavisimosty 7")
        String fromAddress,

        @JsonProperty("toAddress")
        @Schema(example = "Nezavisimosty 183")
        String toAddress,

        @JsonProperty("rideStatus")
        RideStatus rideStatus,

        @PastOrPresent(message = "Date cannot be in the future.")
        @JsonProperty("orderDateTime")
        @Schema(example = "2024-15-03T12:00:00")
        LocalDateTime orderDateTime,

        @JsonProperty("price")
        Float price,

        @JsonProperty("currency")
        Currency currency
) {
}
