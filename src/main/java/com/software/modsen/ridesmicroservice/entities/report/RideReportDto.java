package com.software.modsen.ridesmicroservice.entities.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RideReportDto(
        @NotBlank(message = "format must be init.")
        @JsonProperty("format")
        String format,
        @JsonProperty("startDate")
        LocalDateTime startDate,
        @JsonProperty("endDate")
        LocalDateTime endDate,
        @NotNull(message = "dateType must be init.")
        @JsonProperty("dateType")
        String dateType,
        @Email
        @NotNull(message = "email cannot be empty.")
        @JsonProperty("email")
        String email,
        @Min(value = 0, message = "max number cannot be less then 0.")
        @JsonProperty("rows")
        Integer rows
) {
}
