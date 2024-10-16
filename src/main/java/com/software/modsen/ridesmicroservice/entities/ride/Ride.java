package com.software.modsen.ridesmicroservice.entities.ride;

import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Ride entity.")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "from_address", nullable = false)
    @Schema(example = "Nezavisimosty 7")
    private String fromAddress;

    @Column(name = "to_address", nullable = false)
    @Schema(example = "Nezavisimosty 183")
    private String toAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "ride_status", nullable = false)
    private RideStatus rideStatus;

    @Column(name = "order_date_time", nullable = false)
    @Schema(example = "2024-15-03T12:00:00")
    private LocalDateTime orderDateTime;

    @Column(name = "price", nullable = false)
    private Float price;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
}