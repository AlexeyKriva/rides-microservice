package com.software.modsen.ridesmicroservice.entities.account;

import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "driver_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "balance", nullable = false)
    private Float balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
}
