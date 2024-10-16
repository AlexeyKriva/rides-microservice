package com.software.modsen.ridesmicroservice.entities.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
@Schema(description = "Ride account entity.")
public class RideAccount {
    @NotNull
    @Range(min = 5, message = "You cannot to raise your balance less than 5.")
    @JsonProperty("balance")
    private Float balance;

    @NotNull
    @JsonProperty("currency")
    private Currency currency;
}