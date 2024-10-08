package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.entities.account.RideAccount;

public interface RideAccountObserver {
    void updateBalance(Long userId, RideAccount rideAccount);
}
