package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.account.DriverAccountIncreaseDto;
import com.software.modsen.ridesmicroservice.entities.account.PassengerAccountCancelDto;
import com.software.modsen.ridesmicroservice.entities.account.RideAccount;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class RideAccountPassengerObserverImpl implements RideAccountObserver {
    private PassengerClient passengerClient;

    @Override
    @Transactional
    public void updateBalance(Long passengerId, RideAccount rideAccount) {
        passengerClient.cancelBalanceByPassengerId(
                passengerId,
                new PassengerAccountCancelDto(rideAccount.getBalance(), rideAccount.getCurrency()));
    }
}
