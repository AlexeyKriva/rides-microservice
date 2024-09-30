package com.software.modsen.ridesmicroservice.saga;

import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.account.*;
import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideStatus;
import com.software.modsen.ridesmicroservice.observer.RideAccountSubject;
import com.software.modsen.ridesmicroservice.repositories.RideRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RideSagaCoordinator {
    private RideRepository rideRepository;
    private RideAccountSubject rideAccountSubject;
    private PassengerClient passengerClient;
    private DriverClient driverClient;

    private final Float BALANCE_FACTOR = 0.8f;

    @Retryable(retryFor = {DataAccessException.class, FeignException.class}, maxAttempts = 5,
            backoff = @Backoff(delay = 500))
    public Ride updateRideStatusAndPassengerAndDriverBalances(RideStatus lastRideStatus, Ride updatingRide) {
        try {
            Ride savedRide = rideRepository.save(updatingRide);
            updatingRide.setRideStatus(RideStatus.COMPLETED);

            rideAccountSubject.notifyRideAccountObservers(
                    savedRide.getPassenger().getId(),
                    savedRide.getDriver().getId(),
                    new RideAccount(savedRide.getPrice(), savedRide.getCurrency()));
        } catch (Exception exception) {
            updatingRide.setRideStatus(lastRideStatus);

            rollbackRideStatusAndPassengerAndDriverBalances(updatingRide);
        }

        return updatingRide;
    }

    public void rollbackRideStatusAndPassengerAndDriverBalances(Ride rollbackRide) {
        rideRepository.save(rollbackRide);

        passengerClient.increaseBalanceByPassengerId(
                rollbackRide.getPassenger().getId(),
                new PassengerAccountIncreaseDto(
                        rollbackRide.getPrice(),
                        rollbackRide.getCurrency()));

        driverClient.cancelBalanceByPassengerId(
                rollbackRide.getDriver().getId(),
                new DriverAccountCancelDto(
                        rollbackRide.getPrice() * BALANCE_FACTOR,
                        rollbackRide.getCurrency()));
    }
}
