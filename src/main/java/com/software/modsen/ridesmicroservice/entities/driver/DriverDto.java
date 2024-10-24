package com.software.modsen.ridesmicroservice.entities.driver;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Driver entity.")
public record DriverDto(
        @JsonProperty("name")
        @NotBlank(message = "Name cannot be blank.")
        @Size(min = 4, max = 55, message = "The number of characters in the name" +
                " must be at least 4 and not exceed 55.")
        String name,

        @JsonProperty("email")
        @Email(message = "Invalid email format.")
        @NotBlank(message = "Email cannot be blank.")
        @Schema(example = "user@gmail.com")
        String email,

        @JsonProperty("phoneNumber")
        @Pattern(regexp = "^(?:\\+375|375|80)(?:25|29|33|44|17)\\d{7}$", message = "Invalid phone" +
                " number format.")
        @NotBlank(message = "Phone number cannot be blank.")
        @Schema(example = "+375331234567")
        String phoneNumber,

        @JsonProperty("sex")
        @NotNull(message = "Sex cannot be null.")
        Sex sex,

        @JsonProperty("carId")
        @NotNull(message = "Car id cannot be null.")
        Long carId
) {
}
