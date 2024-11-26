package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.account.PassengerAccountBalanceDownDto;
import com.software.modsen.ridesmicroservice.entities.account.RideAccount;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class RideAccountPassengerObserverImpl implements RideAccountObserver {
    private PassengerClient passengerClient;

    @Override
    public void updateBalance(String passengerId, RideAccount rideAccount) {
        passengerClient.cancelBalanceByPassengerId(
                passengerId,
                new PassengerAccountBalanceDownDto(rideAccount.getBalance(), rideAccount.getCurrency()));
    }

    @Override
    public void updateBalance(Long userId, RideAccount rideAccount) {

    }
}