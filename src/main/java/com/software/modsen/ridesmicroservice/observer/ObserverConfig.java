package com.software.modsen.ridesmicroservice.observer;

import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObserverConfig {
    @Bean
    public RideAccountSubject rideAccountSubject(PassengerClient passengerClient,
                                                 DriverClient driverClient) {
        RideAccountSubject rideAccountSubject = new RideAccountSubject();
        rideAccountSubject.addRideAccountObserver(new RideAccountObserverImpl(passengerClient,
                driverClient) {
        });

        return rideAccountSubject;
    }
}
