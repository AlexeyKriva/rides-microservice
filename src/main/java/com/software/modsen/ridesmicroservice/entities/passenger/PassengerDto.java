package com.software.modsen.ridesmicroservice.entities.passenger;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class PassengerDto {
    @JsonProperty("name")
    @NotBlank(message = "Name cannot be blank.")
    @Size(min = 4, max = 55, message = "The number of characters in the name" +
            " must be at least 4 and not exceed 55.")
    private String name;

    @JsonProperty("email")
    @Email(message = "Invalid email format.")
    @NotBlank(message = "Name cannot be blank.")
    private String email;

    @JsonProperty("phone_number")
    @Pattern(regexp = "^(?:\\+375|375|80)(?:25|29|33|44|17)\\d{7}$", message = "Invalid phone" +
            " number format")
    @NotBlank(message = "Name cannot be blank.")
    private String phoneNumber;
}