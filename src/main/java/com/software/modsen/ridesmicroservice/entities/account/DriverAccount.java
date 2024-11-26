package com.software.modsen.ridesmicroservice.entities.account;

import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Driver account entity.")
public class DriverAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    private Long driverId;

    @Column(name = "balance", nullable = false)
    private Float balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
}