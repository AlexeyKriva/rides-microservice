package com.software.modsen.ridesmicroservice.services;

import static com.software.modsen.ridesmicroservice.exceptions.ExceptionMessage.*;
import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import com.software.modsen.ridesmicroservice.entities.ride.*;
import com.software.modsen.ridesmicroservice.exceptions.DatabaseConnectionRefusedException;
import com.software.modsen.ridesmicroservice.exceptions.RideNotFondException;
import com.software.modsen.ridesmicroservice.repositories.RideRepository;
import com.software.modsen.ridesmicroservice.saga.RideSagaCoordinator;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@EnableScheduling
public class RideService {
    private RideRepository rideRepository;
    private PassengerClient passengerClient;
    private DriverClient driverClient;
    private RideSagaCoordinator rideSagaCoordinator;

    public List<Ride> getAllRides(boolean includeCancelledAndCompleted) {
        log.info("Test log for ELK");
        if (includeCancelledAndCompleted) {
            return rideRepository.findAll();
        } else {
            return rideRepository.findAll().stream()
                    .filter(ride -> ride.getRideStatus() != RideStatus.COMPLETED
                            && ride.getRideStatus() != RideStatus.CANCELLED
                    )
                    .collect(Collectors.toList());
        }
    }

    public List<Ride> getAllRidesByPassengerId(String passengerId) {
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

    @CircuitBreaker(name = "simpleCircuitBreaker", fallbackMethod = "fallbackPostgresHandle")
    @Transactional
    public Ride saveRide(String passengerId, Long driverId, Ride newRide) {
        ResponseEntity<Passenger> passengerFromDb = passengerClient.getPassengerById(passengerId);
        ResponseEntity<Driver> driverFromDb = driverClient.getDriverById(driverId);

        if (passengerFromDb.getBody() != null && driverFromDb.getBody() != null) {
            newRide.setPassengerId(passengerFromDb.getBody().getId());
            newRide.setDriverId(driverFromDb.getBody().getId());
            newRide.setRideStatus(RideStatus.CREATED);

            return rideRepository.save(newRide);
        }

        return new Ride();
    }

    @Transactional
    public Ride updateRide(long id, String passengerId, Long driverId, Ride updatingRide) {
        ResponseEntity<Passenger> passengerFromDb = passengerClient.getPassengerById(passengerId);
        ResponseEntity<Driver> driverFromDb = driverClient.getDriverById(driverId);

        Optional<Ride> rideFromDb = rideRepository.findById(id);

        if (rideFromDb.isPresent()) {
            updatingRide.setId(id);
            updatingRide.setPassengerId(passengerFromDb.getBody().getId());
            updatingRide.setDriverId(driverFromDb.getBody().getId());

            return rideRepository.save(updatingRide);
        }

        throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);
    }

    @Transactional
    public Ride patchRide(long id, String passengerId, Long driverId, Ride updatingRide) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        if (rideFromDb.isPresent()) {
            updatingRide.setId(id);

            ResponseEntity<Passenger> passengerFromDb;

            if (passengerId == null) {
                passengerFromDb = passengerClient.getPassengerById(
                        rideFromDb.get().getPassengerId());
            } else {
                passengerFromDb = passengerClient.getPassengerById(passengerId);
            }

            updatingRide.setPassengerId(passengerFromDb.getBody().getId());

            ResponseEntity<Driver> driverFromDb;

            if (driverId == null) {
                driverFromDb = driverClient.getDriverById(
                        rideFromDb.get().getDriverId());
            } else {
                driverFromDb = driverClient.getDriverById(driverId);
            }

            updatingRide.setDriverId(driverFromDb.getBody().getId());

            if (updatingRide.getFromAddress() == null) {
                updatingRide.setFromAddress(rideFromDb.get().getFromAddress());
            }
            if (updatingRide.getToAddress() == null) {
                updatingRide.setToAddress(rideFromDb.get().getToAddress());
            }
            if (updatingRide.getRideStatus() == null) {
                updatingRide.setRideStatus(rideFromDb.get().getRideStatus());
            }
            if (updatingRide.getOrderDateTime() == null) {
                updatingRide.setOrderDateTime(rideFromDb.get().getOrderDateTime());
            }
            if (updatingRide.getPrice() == null) {
                updatingRide.setPrice(rideFromDb.get().getPrice());
            }
            if (updatingRide.getCurrency() == null) {
                updatingRide.setCurrency(rideFromDb.get().getCurrency());
            }

            return rideRepository.save(updatingRide);
        }

        throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);
    }

    public Ride changeRideStatusById(long id, RideStatus rideStatus) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        if (rideFromDb.isPresent()) {
            Ride updatingRide = rideFromDb.get();

            if (rideStatus.equals(RideStatus.COMPLETED)) {
                return rideSagaCoordinator.updateRideStatusAndPassengerAndDriverBalances(updatingRide.getRideStatus(),
                        updatingRide);
            } else {
                updatingRide.setRideStatus(rideStatus);

                return rideRepository.save(updatingRide);
            }
        }

        throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);
    }

    @Transactional
    public void deleteRideById(long id) {
        Optional<Ride> rideFromDb = rideRepository.findById(id);

        rideFromDb.ifPresentOrElse(
                ride -> rideRepository.deleteById(id),
                () -> {throw new RideNotFondException(RIDE_NOT_FOUND_MESSAGE);}
        );
    }

    @Transactional
    public void deleteRideByPassengerId(String passengerId) {
        rideRepository.deleteAllByPassengerId(passengerId);
    }

    @Transactional
    public void deleteRideByDriverId(long driverId) {
        rideRepository.deleteAllByDriverId(driverId);
    }

    @Recover
    public Ride fallbackPostgresHandle(Throwable throwable) {
        if (throwable instanceof FeignException) {
            throw (FeignException) throwable;
        } else if (throwable instanceof DataIntegrityViolationException) {
            throw (DataIntegrityViolationException) throwable;
        }

        throw new DatabaseConnectionRefusedException(BAD_CONNECTION_TO_DATABASE_MESSAGE + CANNOT_UPDATE_DATA_MESSAGE);
    }

    //@Scheduled(fixedRate = 3000)
    public void testLogsForELK() {
        log.info("User make query to server.");
        log.warn("Something happened...");
    }
}