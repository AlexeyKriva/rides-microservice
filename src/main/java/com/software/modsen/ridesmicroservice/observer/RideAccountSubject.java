package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.entities.account.RideAccount;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class RideAccountSubject {
    private List<RideAccountObserver> rideAccountObservers = new ArrayList<>();

    public void addRideAccountObserver(RideAccountObserver rideAccountObserver) {
        rideAccountObservers.add(rideAccountObserver);
    }

    public void removeRideAccountObserver(RideAccountObserver rideAccountObserver) {
        rideAccountObservers.remove(rideAccountObserver);
    }

    @Transactional
    public void notifyRideAccountObservers(Long passengerId, Long driverId, RideAccount rideAccount) {
        rideAccountObservers.get(0).updateBalance(passengerId, rideAccount);
        rideAccountObservers.get(1).updateBalance(driverId, rideAccount);
    }
}
