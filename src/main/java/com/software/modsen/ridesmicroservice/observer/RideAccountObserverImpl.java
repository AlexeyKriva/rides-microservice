package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccountIncreaseDto;
import com.software.modsen.ridesmicroservice.entities.account.PassengerAccountCancelDto;
import com.software.modsen.ridesmicroservice.entities.account.RideAccount;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class RideAccountObserverImpl implements RideAccountObserver {
    private PassengerClient passengerClient;
    private DriverClient driverClient;
    private final Float BALANCE_FACTOR = 0.8f;

    @Override
    @Transactional
    public void updatePassengerAndDriverBalances(Long passengerId, Long driverId, RideAccount rideAccount) {
        passengerClient.cancelBalanceByPassengerId(
                passengerId,
                new PassengerAccountCancelDto(rideAccount.getBalance(), rideAccount.getCurrency()));

        driverClient.increaseBalanceByDriverId(
                driverId,
                new DriverAccountIncreaseDto(rideAccount.getBalance() * BALANCE_FACTOR, rideAccount.getCurrency()));
    }
}
