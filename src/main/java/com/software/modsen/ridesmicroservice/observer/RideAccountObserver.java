package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.entities.account.RideAccount;

public interface RideAccountObserver {
    void updatePassengerAndDriverBalances(Long passengerId, Long driverId, RideAccount rideAccount);
}
