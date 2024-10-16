package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccountIncreaseDto;
import com.software.modsen.ridesmicroservice.entities.account.RideAccount;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class RideAccountDriverObserverImpl implements RideAccountObserver {
    private DriverClient driverClient;
    private final Float BALANCE_FACTOR = 0.8f;

    @Override
    @Transactional
    public void updateBalance(Long driverId, RideAccount rideAccount) {
        driverClient.increaseBalanceByDriverId(
                driverId,
                new DriverAccountIncreaseDto(
                        rideAccount.getBalance() * BALANCE_FACTOR, rideAccount.getCurrency()));
    }
}