package com.software.modsen.ridesmicroservice.services;

import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.Driver.Driver;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideDto;
import com.software.modsen.ridesmicroservice.entities.ride.RidePatchDto;
import com.software.modsen.ridesmicroservice.entities.ride.RideStatus;
import com.software.modsen.ridesmicroservice.mappers.RideMapper;
import com.software.modsen.ridesmicroservice.repositories.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RideServiceImpl {
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private PassengerClient passengerClient;
    @Autowired
    private DriverClient driverClient;
    private final RideMapper RIDE_MAPPER = RideMapper.INSTANCE;

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public List<Ride> getAllNotCompletedAndNotCancelledRides() {
        return rideRepository.findAll().stream()
                .filter(ride -> (!ride.getRideStatus().equals(RideStatus.COMPLETED) &&
                !ride.getRideStatus().equals(RideStatus.CANCELLED)))
                .collect(Collectors.toList());
    }

    public Ride getRideById(long id) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);
        if (rideFromDb.isPresent()) {
            return rideFromDb.get();
        }

        throw new RuntimeException();
    }

    public Ride getNotCompletedAndNotCancelledRideById(long id) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);
        if (rideFromDb.isPresent()) {
            if (!rideFromDb.get().getRideStatus().equals(RideStatus.COMPLETED) &&
                    !rideFromDb.get().getRideStatus().equals(RideStatus.CANCELLED)) {
                return rideFromDb.get();
            }

            throw new RuntimeException();
        }

        throw new RuntimeException();
    }

    public Ride saveRide(RideDto rideDto) {
        Ride newRide = RIDE_MAPPER.fromRideDtoToRide(rideDto);
        return rideRepository.save(newRide);
    }

    public Ride updateRide(long id, RideDto rideDto) {
        ResponseEntity<Passenger> passengerFromDb = passengerClient.getPassengerById(rideDto.getPassengerId());
        ResponseEntity<Driver> driverFromDb = driverClient.getDriverById(rideDto.getDriverId());
        if (passengerFromDb.getBody() != null && driverFromDb.getBody() != null) {
            Optional<Ride> rideFromDb = rideRepository.findById(id);
            if (rideFromDb.isPresent()) {
                Ride updatingRide = RIDE_MAPPER.fromRideDtoToRide(rideDto);
                updatingRide.setId(id);
                updatingRide.setPassenger(passengerFromDb.getBody());
                updatingRide.setDriver(driverFromDb.getBody());

                return rideRepository.save(updatingRide);
            }

            throw new RuntimeException();
        }

        throw new RuntimeException();
    }

    public Ride patchRide(long id, RidePatchDto ridePatchDto) {
        ResponseEntity<Passenger> passengerFromDb = passengerClient.getPassengerById(ridePatchDto.getPassengerId());
        ResponseEntity<Driver> driverFromDb = driverClient.getDriverById(ridePatchDto.getDriverId());
        if (passengerFromDb.getBody() != null && driverFromDb.getBody() != null) {
            Optional<Ride> rideFromDb = rideRepository.findById(id);
            if (rideFromDb.isPresent()) {
                Ride updatingRide = rideFromDb.get();
                RIDE_MAPPER.updateRideFromRidePatchDto(ridePatchDto, updatingRide);

                return rideRepository.save(updatingRide);
            }

            throw new RuntimeException();
        }

        throw new RuntimeException();
    }

    public Ride changeRideStatusById(long id, RideStatus rideStatus) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        return rideFromDb.map(ride -> {
            ride.setRideStatus(rideStatus);
            return rideRepository.save(ride);
        }).orElseThrow(() -> new RuntimeException());
    }
}