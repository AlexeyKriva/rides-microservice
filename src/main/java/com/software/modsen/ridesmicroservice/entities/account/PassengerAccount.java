package com.software.modsen.ridesmicroservice.entities.account;

import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passenger_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Passenger account entity.")
public class PassengerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    private String passengerId;

    @Column(name = "balance", nullable = false)
    private Float balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
}
