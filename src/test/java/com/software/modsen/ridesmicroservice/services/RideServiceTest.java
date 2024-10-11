package com.software.modsen.ridesmicroservice.services;

import com.software.modsen.ridesmicroservice.clients.DriverClient;
import com.software.modsen.ridesmicroservice.clients.PassengerClient;
import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import com.software.modsen.ridesmicroservice.entities.driver.Sex;
import com.software.modsen.ridesmicroservice.entities.driver.car.Car;
import com.software.modsen.ridesmicroservice.entities.driver.car.CarBrand;
import com.software.modsen.ridesmicroservice.entities.driver.car.CarColor;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import com.software.modsen.ridesmicroservice.entities.ride.Currency;
import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideStatus;
import com.software.modsen.ridesmicroservice.exceptions.RideNotFondException;
import com.software.modsen.ridesmicroservice.repositories.RideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.software.modsen.ridesmicroservice.exceptions.ExceptionMessage.RIDE_NOT_FOUND_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceTest {
    @Mock
    RideRepository rideRepository;

    @Mock
    PassengerClient passengerClient;

    @Mock
    DriverClient driverClient;

    @InjectMocks
    RideService rideService;

    Passenger passengerWithIdAndIsDeleted(long id, boolean isDeleted) {
        return new Passenger(id, "name" + id, "name" + id + "@gmail.com",
                "+375299312345", isDeleted);
    }

    Driver driverWithIdAndIsDeleted(long id, boolean isDeleted) {
        return new Driver(id, "name" + id, "name" + id + "@gmail.com",
                "+375299312345", Sex.MALE, new Car(id, CarColor.GREEN, CarBrand.AUDI, "123" + id + "AB-1",
                isDeleted), isDeleted);
    }

    Ride defaultRide() {
        return new Ride(1, passengerWithIdAndIsDeleted(1L, false),
                driverWithIdAndIsDeleted(1L, false),
                "Nezavisimosty 1", "Nezavisimosty 2", RideStatus.CREATED,
                LocalDateTime.of(2024, 10, 1, 12, 0, 0, 0),
                100f, Currency.BYN);
    }

    List<Ride> defaultRides(List<Long> passengerIdes, List<Long> driverIdes) {
        return List.of(
                new Ride(1, passengerWithIdAndIsDeleted(passengerIdes.get(0), false),
                        driverWithIdAndIsDeleted(driverIdes.get(0), false),
                        "Nezavisimosty 1", "Nezavisimosty 2", RideStatus.CREATED,
                        LocalDateTime.of(2024, 10, 1, 12, 0, 0, 0),
                        100f, Currency.BYN),
                new Ride(2, passengerWithIdAndIsDeleted(passengerIdes.get(1), false),
                        driverWithIdAndIsDeleted(driverIdes.get(1), false),
                        "Nezavisimosty 3", "Nezavisimosty 4", RideStatus.CREATED,
                        LocalDateTime.of(2024, 10, 2, 12, 0, 0, 0),
                        100f, Currency.BYN)
        );
    }

    @Test
    void getAllRidesTest_ReturnsRides() {
        //given
        List<Ride> rides = defaultRides(List.of(1L, 2L), List.of(1L, 2L));
        doReturn(rides).when(rideRepository).findAll();

        //when
        List<Ride> ridesFromDb = rideService.getAllRides();

        //then
        assertNotNull(ridesFromDb);
        assertEquals(rides, ridesFromDb);
    }

    @Test
    void getAllNotCompletedAndNotCancelledRidesTest_ReturnsValidRides() {
        //given
        List<Ride> rides = defaultRides(List.of(1L, 2L), List.of(1L, 2L));
        doReturn(rides).when(rideRepository).findAll();

        //when
        List<Ride> ridesFromDb = rideService.getAllRides();

        //then
        assertNotNull(ridesFromDb);
        assertEquals(rides, ridesFromDb);
        assertTrue(ridesFromDb.stream().allMatch(ride ->
                ride.getRideStatus() != RideStatus.COMPLETED && ride.getRideStatus() != RideStatus.CANCELLED
        ));
    }

    @Test
    void getAllRidesByPassengerIdTest_ReturnsValidRides() {
        //given
        long passengerId = 1L;
        List<Ride> rides = defaultRides(List.of(1L, 1L), List.of(1L, 2L));
        doReturn(rides).when(rideRepository).findAllByPassengerId(passengerId);

        //when
        List<Ride> ridesFromDb = rideService.getAllRidesByPassengerId(passengerId);

        //then
        assertNotNull(ridesFromDb);
        assertEquals(rides, ridesFromDb);
        assertTrue(ridesFromDb.stream().allMatch(ride ->
                ride.getPassenger().getId() == passengerId));
    }

    @Test
    void getAllRidesByDriverIdTest_ReturnsValidRides() {
        //given
        long driverId = 1L;
        List<Ride> rides = defaultRides(List.of(1L, 2L), List.of(1L, 1L));
        doReturn(rides).when(rideRepository).findAllByDriverId(driverId);

        //when
        List<Ride> ridesFromDb = rideService.getAllRidesByDriverId(driverId);

        //then
        assertNotNull(ridesFromDb);
        assertEquals(rides, ridesFromDb);
        assertTrue(ridesFromDb.stream().allMatch(ride ->
                ride.getDriver().getId() == driverId));
    }

    @Test
    void getRideByIdTest_WithoutException_ReturnsRide() {
        //given
        long rideId = 1L;
        Optional<Ride> ride = Optional.of(defaultRide());
        doReturn(ride).when(rideRepository).findById(rideId);

        //when
        Ride rideFromDb = rideService.getRideById(rideId);

        //then
        assertNotNull(rideFromDb);
        assertEquals(ride.get(), rideFromDb);
    }

    @Test
    void getRideByIdTest_WithRideNotFoundException_ReturnsRide() {
        //given
        long rideId = 2L;
        Optional<Ride> ride = Optional.of(defaultRide());
        doThrow(new RideNotFondException(RIDE_NOT_FOUND_MESSAGE)).when(rideRepository).findById(rideId);

        //when
        RideNotFondException exception = assertThrows(RideNotFondException.class,
                () -> rideService.getRideById(rideId));

        //then
        assertEquals(RIDE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void saveRideTest_ReturnsRide() {
        //given
        long passengerId = 1L;
        long driverId = 1L;
        Ride newRide = new Ride(1L, null, null,
                "Nezavisimosty 1", "Nezavisimosty 2", null,
                LocalDateTime.of(2024, 10, 3, 12, 0, 0, 0),
                100f, Currency.BYN);
        ResponseEntity<Passenger> passengerEntity = new ResponseEntity<>(
                passengerWithIdAndIsDeleted(passengerId, false), HttpStatus.OK);
        ResponseEntity<Driver> driverEntity = new ResponseEntity<>(
                driverWithIdAndIsDeleted(driverId, false), HttpStatus.OK);
        doReturn(passengerEntity).when(passengerClient).getPassengerById(passengerId);
        doReturn(driverEntity).when(driverClient).getDriverById(driverId);
        Ride savedride = newRide;
        savedride.setPassenger(passengerEntity.getBody());
        savedride.setDriver(driverEntity.getBody());
        savedride.setRideStatus(RideStatus.CREATED);
        doReturn(savedride).when(rideRepository).save(savedride);

        //when
        Ride rideFromDb = rideService.saveRide(passengerId, driverId, newRide);

        //then
        assertNotNull(rideFromDb);
        assertEquals(savedride, rideFromDb);
        verify(rideRepository).save(savedride);
    }

    @Test
    void updateRideTest_WithoutException_ReturnsRide() {
        //given
        long rideId = 1L;
        long passengerId = 1L;
        long driverId = 1L;
        ResponseEntity<Passenger> passengerEntity = new ResponseEntity<>(
                passengerWithIdAndIsDeleted(passengerId, false), HttpStatus.OK);
        ResponseEntity<Driver> driverEntity = new ResponseEntity<>(
                driverWithIdAndIsDeleted(driverId, false), HttpStatus.OK);
        doReturn(passengerEntity).when(passengerClient).getPassengerById(passengerId);
        doReturn(driverEntity).when(driverClient).getDriverById(driverId);
        Ride updatingPassenger = new Ride(rideId, null, null,
                "Nezavisimosty 3", "Nezavisimosty 7", RideStatus.EN_ROUTE_TO_DESTINATION,
                LocalDateTime.of(2024, 9, 12, 12, 0, 0, 0),
                100f, Currency.BYN);
        Optional<Ride> ride = Optional.of(defaultRide());
        doReturn(ride).when(rideRepository).findById(rideId);
        Ride savedRide = new Ride(rideId, passengerEntity.getBody(), driverEntity.getBody(), updatingPassenger
                .getFromAddress(),
                updatingPassenger.getToAddress(), updatingPassenger.getRideStatus(), updatingPassenger
                .getOrderDateTime(), updatingPassenger.getPrice(),
                updatingPassenger.getCurrency());
        doReturn(savedRide).when(rideRepository).save(savedRide);

        //when
        Ride rideFromDB = rideService.updateRide(rideId, passengerId, driverId, updatingPassenger);

        //then
        assertNotNull(rideFromDB);
        assertEquals(savedRide, rideFromDB);
        verify(rideRepository).save(savedRide);
    }

    @Test
    void updateRideTest_WithRideNotFoundException_ReturnsException() {
        //given
        long rideId = 1L;
        long passengerId = 1L;
        long driverId = 1L;
        Ride updatingRide = new Ride(rideId, null, null,
                "Nezavisimosty 3", "Nezavisimosty 7", RideStatus.EN_ROUTE_TO_DESTINATION,
                LocalDateTime.of(2024, 9, 12, 12, 0, 0, 0),
                100f, Currency.BYN);
        doThrow(new RideNotFondException(RIDE_NOT_FOUND_MESSAGE)).when(rideRepository).findById(rideId);

        //when
        RideNotFondException exception = assertThrows(RideNotFondException.class,
                () -> rideService.updateRide(rideId, passengerId, driverId, updatingRide)
        );

        //then
        assertEquals(RIDE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void patchRide_WithoutException_ReturnsRide() {
        //given
        long rideId = 1L;
        long passengerId = 1L;
        long driverId = 1L;
        ResponseEntity<Passenger> passengerEntity = new ResponseEntity<>(
                passengerWithIdAndIsDeleted(passengerId, false), HttpStatus.OK);
        ResponseEntity<Driver> driverEntity = new ResponseEntity<>(
                driverWithIdAndIsDeleted(driverId, false), HttpStatus.OK);
        doReturn(passengerEntity).when(passengerClient).getPassengerById(passengerId);
        doReturn(driverEntity).when(driverClient).getDriverById(driverId);
        Ride patchRide = new Ride(0, null, null,
                "Nezavisimosty 3", "Nezavisimosty 7", RideStatus.EN_ROUTE_TO_DESTINATION,
                null,
                100f, Currency.BYN);
        Optional<Ride> ride = Optional.of(defaultRide());
        doReturn(ride).when(rideRepository).findById(rideId);
        Ride savedRide = patchRide;
        savedRide.setId(rideId);
        savedRide.setPassenger(passengerEntity.getBody());
        savedRide.setDriver(driverEntity.getBody());
        savedRide.setOrderDateTime(ride.get().getOrderDateTime());
        doReturn(savedRide).when(rideRepository).save(savedRide);

        //when
        Ride rideFromDB = rideService.patchRide(rideId, passengerId, driverId, patchRide);

        //then
        assertNotNull(rideFromDB);
        assertEquals(savedRide, rideFromDB);
        verify(rideRepository).save(savedRide);
    }

    @Test
    void patchRideTest_WithRideNotFoundException_ReturnsException() {
        //given
        long rideId = 1L;
        long passengerId = 1L;
        long driverId = 1L;
        Ride updatingRide = new Ride(rideId, null, null,
                "Nezavisimosty 3", "Nezavisimosty 7", RideStatus.EN_ROUTE_TO_DESTINATION,
                LocalDateTime.of(2024, 9, 12, 12, 0, 0, 0),
                100f, Currency.BYN);
        doThrow(new RideNotFondException(RIDE_NOT_FOUND_MESSAGE)).when(rideRepository).findById(rideId);

        //when
        RideNotFondException exception = assertThrows(RideNotFondException.class,
                () -> rideService.patchRide(rideId, passengerId, driverId, updatingRide));

        //then
        assertEquals(RIDE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void changeRideStatusByIdTest_WithoutException_ReturnsRide() {
        //given
        long rideId = 1;
        RideStatus rideStatus = RideStatus.EN_ROUTE_TO_PASSENGER;
        Optional<Ride> ride = Optional.of(defaultRide());
        doReturn(ride).when(rideRepository).findById(rideId);
        ride.get().setRideStatus(rideStatus);
        doReturn(ride.get()).when(rideRepository).save(ride.get());

        //when
        Ride rideFromDb = rideService.changeRideStatusById(rideId, rideStatus);

        //then
        assertNotNull(rideFromDb);
        assertEquals(ride.get(), rideFromDb);
    }

    @Test
    void changeRideStatusByIdTest_WithRideNotFoundException_ReturnsException() {
        //given
        long rideId = 1;
        RideStatus rideStatus = RideStatus.EN_ROUTE_TO_PASSENGER;
        doThrow(new RideNotFondException(RIDE_NOT_FOUND_MESSAGE)).when(rideRepository).findById(rideId);

        //when
        RideNotFondException rideNotFondException = assertThrows(RideNotFondException.class,
                () -> rideService.changeRideStatusById(rideId, rideStatus));

        //then
        assertEquals(RIDE_NOT_FOUND_MESSAGE, rideNotFondException.getMessage());
    }

    @Test
    void deleteRideByIdTest_WithoutException_ReturnsVoid() {
        //given
        long rideId = 1;
        Optional<Ride> ride = Optional.of(defaultRide());
        doReturn(ride).when(rideRepository).findById(rideId);
        doNothing().when(rideRepository).deleteById(rideId);

        //when
        rideService.deleteRideById(rideId);

        //then

        verify(rideRepository).deleteById(rideId);
    }

    @Test
    void deleteRideByIdTest_WithRideNotFoundException_ReturnsException() {
        //given
        long rideId = 1;
        doThrow(new RideNotFondException(RIDE_NOT_FOUND_MESSAGE)).when(rideRepository).findById(rideId);

        //when
        RideNotFondException exception = assertThrows(RideNotFondException.class,
                () -> rideService.deleteRideById(rideId));

        //then

        assertEquals(RIDE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void deleteRideByPassengerIdTest_ReturnsVoid() {
        //given
        long passengerId = 1;
        doNothing().when(rideRepository).deleteAllByPassengerId(passengerId);

        //when
        rideService.deleteRideByPassengerId(passengerId);

        //then

        verify(rideRepository).deleteAllByPassengerId(passengerId);
    }

    @Test
    void deleteRideByDriverIdTest_ReturnsVoid() {
        //given
        long driverId = 1;
        doNothing().when(rideRepository).deleteAllByDriverId(driverId);

        //when
        rideService.deleteRideByDriverId(driverId);

        //then

        verify(rideRepository).deleteAllByDriverId(driverId);
    }
}