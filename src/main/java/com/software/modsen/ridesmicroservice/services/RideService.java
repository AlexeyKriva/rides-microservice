package com.software.modsen.ridesmicroservice.services;

import static com.software.modsen.ridesmicroservice.exceptions.ExceptionMessage.*;
import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import com.software.modsen.ridesmicroservice.entities.ride.*;
import com.software.modsen.ridesmicroservice.exceptions.RideNotFondException;
import com.software.modsen.ridesmicroservice.mappers.RideMapper;
import com.software.modsen.ridesmicroservice.repositories.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RideService {
    private RideRepository rideRepository;
    private PassengerClient passengerClient;
    private DriverClient driverClient;
    private final RideMapper RIDE_MAPPER = RideMapper.INSTANCE;

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public List<Ride> getAllNotCompletedAndNotCancelledRides() {
        return rideRepository.findAll().stream()
                .filter(ride -> ride.getRideStatus() != RideStatus.COMPLETED
                        && ride.getRideStatus() != RideStatus.CANCELLED
                )
                .collect(Collectors.toList());
    }

    public List<Ride> getAllRidesByPassengerId(long passengerId) {
        return rideRepository.findAllByPassengerId(passengerId);
    }

    public List<Ride> getAllRidesByDriverId(long driverId) {
        return rideRepository.findAllByDriverId(driverId);
    }

    public Ride getRideById(long id) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        if (rideFromDb.isPresent()) {
            return rideFromDb.get();
        }

        throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);
    }

    public Ride getNotCompletedAndNotCancelledRideById(long id) {
        Optional<Ride> rideFromDb = rideRepository.findByIdAndRideStatus(id, RideStatus.COMPLETED,
                RideStatus.CANCELLED);

        if (rideFromDb.isPresent()) {
            return rideFromDb.get();
        }

        throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);
    }

    public Ride saveRide(RideDto rideDto) {
        ResponseEntity<Passenger> passengerFromDb = passengerClient.getPassengerById(rideDto.getPassengerId());
        ResponseEntity<Driver> driverFromDb = driverClient.getDriverById(rideDto.getDriverId());

        if (passengerFromDb.getBody() != null && driverFromDb.getBody() != null) {
            Ride newRide = RIDE_MAPPER.fromRideDtoToRide(rideDto);
            newRide.setPassenger(passengerFromDb.getBody());
            newRide.setDriver(driverFromDb.getBody());
            newRide.setRideStatus(RideStatus.CREATED);

            return rideRepository.save(newRide);
        }

        return new Ride();
    }

    public Ride updateRide(long id, RidePutDto ridePutDto) {
        ResponseEntity<Passenger> passengerFromDb = passengerClient.getPassengerById(ridePutDto.getPassengerId());
        ResponseEntity<Driver> driverFromDb = driverClient.getDriverById(ridePutDto.getDriverId());

        Optional<Ride> rideFromDb = rideRepository.findById(id);

        if (rideFromDb.isPresent()) {
            Ride updatingRide = RIDE_MAPPER.fromRidePutDtoToRide(ridePutDto);
            updatingRide.setId(id);
            updatingRide.setPassenger(passengerFromDb.getBody());
            updatingRide.setDriver(driverFromDb.getBody());

            return rideRepository.save(updatingRide);
        }

        throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);
    }

    public Ride patchRide(long id, RidePatchDto ridePatchDto) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        if (rideFromDb.isPresent()) {
            Ride updatingRide = rideFromDb.get();
            RIDE_MAPPER.updateRideFromRidePatchDto(ridePatchDto, updatingRide);

            if (ridePatchDto.getPassengerId() != null) {
                ResponseEntity<Passenger> passengerFromDb = passengerClient.getPassengerById(ridePatchDto.getPassengerId());
                updatingRide.setPassenger(passengerFromDb.getBody());
            }
            if (ridePatchDto.getDriverId() != null) {
                ResponseEntity<Driver> driverFromDb = driverClient.getDriverById(ridePatchDto.getDriverId());
                updatingRide.setDriver(driverFromDb.getBody());
            }

            return rideRepository.save(updatingRide);
        }

        throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);
    }

    public Ride changeRideStatusById(long id, RideStatus rideStatus) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        return rideFromDb.map(ride -> {
            ride.setRideStatus(rideStatus);
            return rideRepository.save(ride);
        }).orElseThrow(() -> new RideNotFondException(RIDE_NOT_FOUND_MESSAGE));
    }

    public void deleteRideById(long id) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        rideFromDb.ifPresentOrElse(
                ride -> rideRepository.deleteById(id),
                () -> {throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);}
        );
    }
}