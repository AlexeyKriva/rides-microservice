package com.software.modsen.ridesmicroservice.entities.driver.car;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Car entity.")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private CarColor color;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand", nullable = false)
    private CarBrand brand;

    @Column(name = "car_number", nullable = false)
    @Schema(example = "9999AB-1")
    private String carNumber;

    @Column(name = "is_deleted", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean isDeleted;
}