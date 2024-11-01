package com.software.modsen.ridesmicroservice.entities.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

@Schema(description = "Entity to cancel driver balance.")
public record DriverAccountBalanceUpDto(
        @NotNull
        @Range(min = 5, message = "You cannot to raise your balance less than 5.")
        @JsonProperty("balance")
        Float balance,

        @NotNull
        @JsonProperty("currency")
        Currency currency
) {
}