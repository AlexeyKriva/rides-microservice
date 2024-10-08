package com.software.modsen.ridesmicroservice.entities.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class DriverAccountCancelDto {
    @NotNull
    @Range(min = 0, message = "You cannot to reduce your balance less than 0.")
    @JsonProperty("balance")
    private Float balance;

    @NotNull
    @JsonProperty("currency")
    private Currency currency;
}
