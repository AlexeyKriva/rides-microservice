package com.software.modsen.ridesmicroservice.entities.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
@Builder
@Schema(description = "Ride entity.")
public class RideDto {
    @NotNull(message = "Passenger id cannot be null.")
    @JsonProperty("passenger_id")
    private Long passengerId;

    @NotNull(message = "Driver id cannot be null.")
    @JsonProperty("driver_id")
    private Long driverId;

    @NotBlank(message = "Departure address cannot be blank.")
    @JsonProperty("from_address")
    @Schema(example = "Nezavisimosty 7")
    private String fromAddress;

    @NotBlank(message = "Arrival address cannot be blank.")
    @JsonProperty("to_address")
    @Schema(example = "Nezavisimosty 183")
    private String toAddress;

    @PastOrPresent(message = "Date cannot be in the future.")
    @NotNull(message = "Order date time cannot be null.")
    @JsonProperty("order_date_time")
    @Schema(example = "2024-10-03T12:00:00")
    private LocalDateTime orderDateTime;

    @NotNull(message = "Price cannot be null.")
    @JsonProperty("price")
    private Float price;

    @NotNull(message = "Currency cannot be null.")
    @JsonProperty("currency")
    private Currency currency;
}