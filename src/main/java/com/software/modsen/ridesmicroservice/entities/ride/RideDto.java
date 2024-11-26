package com.software.modsen.ridesmicroservice.entities.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

@Schema(description = "Ride entity.")
public record RideDto(
        @NotNull(message = "Passenger id cannot be null.")
        @JsonProperty("passengerId")
        String passengerId,

        @NotNull(message = "Driver id cannot be null.")
        @JsonProperty("driverId")
        Long driverId,

        @NotBlank(message = "Departure address cannot be blank.")
        @JsonProperty("fromAddress")
        @Schema(example = "Nezavisimosty 7")
        String fromAddress,

        @NotBlank(message = "Arrival address cannot be blank.")
        @JsonProperty("toAddress")
        @Schema(example = "Nezavisimosty 183")
        String toAddress,

        @PastOrPresent(message = "Date cannot be in the future.")
        @NotNull(message = "Order date time cannot be null.")
        @JsonProperty("orderDateTime")
        @Schema(example = "2024-10-03T12:00:00")
        LocalDateTime orderDateTime,

        @NotNull(message = "Price cannot be null.")
        @JsonProperty("price")
        Float price,

        @NotNull(message = "Currency cannot be null.")
        @JsonProperty("currency")
        Currency currency
) {
}