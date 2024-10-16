package com.software.modsen.ridesmicroservice.entities.driver;

import com.software.modsen.ridesmicroservice.entities.driver.car.Car;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Driver entity.")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    @Schema(example = "driver@gmail.com")
    private String email;

    @Column(name = "phone_number", nullable = false)
    @Schema(example = "+375449870563")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private Sex sex;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "is_deleted", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean isDeleted;
}