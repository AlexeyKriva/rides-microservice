package com.software.modsen.ridesmicroservice.controllers;

import com.software.modsen.ridesmicroservice.entities.driver.Driver;
import com.software.modsen.ridesmicroservice.entities.driver.Sex;
import com.software.modsen.ridesmicroservice.entities.driver.car.Car;
import com.software.modsen.ridesmicroservice.entities.driver.car.CarBrand;
import com.software.modsen.ridesmicroservice.entities.driver.car.CarColor;
import com.software.modsen.ridesmicroservice.entities.passenger.Passenger;
import com.software.modsen.ridesmicroservice.entities.ride.*;
import com.software.modsen.ridesmicroservice.mappers.RideMapper;
import com.software.modsen.ridesmicroservice.services.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideControllerTest {
    @Mock
    RideService rideService;

    @Mock
    RideMapper rideMapper;

    @InjectMocks
    RideController rideController;

    @BeforeEach
    void setUp() {
        rideMapper = RideMapper.INSTANCE;
    }

    Ride defaultRide() {
        return new Ride(1, "1",
                1L,
                "Nezavisimosty 1", "Nezavisimosty 2", RideStatus.CREATED,
                LocalDateTime.of(2024, 10, 1, 12, 0, 0, 0),
                100f, Currency.BYN);
    }

    Passenger passengerWithIdAndIsDeleted(String id, boolean isDeleted) {
        return new Passenger(id, "name" + id, "name" + id + "@gmail.com",
                "+375299312345", isDeleted);
    }

    Driver driverWithIdAndIsDeleted(long id, boolean isDeleted) {
        return new Driver(id, "name" + id, "name" + id + "@gmail.com",
                "+375299312345", Sex.MALE, new Car(id, CarColor.GREEN, CarBrand.AUDI, "123" + id + "AB-1",
                isDeleted), isDeleted);
    }

    List<Ride> defaultRides(List<Long> passengerIdes, List<Long> driverIdes) {
        return List.of(
                new Ride(1, "1",
                        1L,
                        "Nezavisimosty 1", "Nezavisimosty 2", RideStatus.CREATED,
                        LocalDateTime.of(2024, 10, 1, 12, 0, 0, 0),
                        100f, Currency.BYN),
                new Ride(2, "2",
                        2L,
                        "Nezavisimosty 3", "Nezavisimosty 4", RideStatus.CREATED,
                        LocalDateTime.of(2024, 10, 2, 12, 0, 0, 0),
                        100f, Currency.BYN)
        );
    }

    @Test
    void getAllRidesTest_ReturnsRides() {
        //given
        List<Ride> rides = defaultRides(List.of(1L, 2L), List.of(1L, 2L));
        doReturn(rides).when(rideService).getAllRides(true);

        //when
        ResponseEntity<List<Ride>> responseEntity = rideController.getAllRides(true);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rides, responseEntity.getBody());
        verify(rideService).getAllRides(true);
    }

    @Test
    void getAllNotCompletedOrCancelledRidesTest_ReturnsValidRides() {
        //given
        List<Ride> rides = defaultRides(List.of(1L, 2L), List.of(1L, 2L));
        doReturn(rides).when(rideService).getAllRides(false);

        //when
        ResponseEntity<List<Ride>> responseEntity = rideController.getAllRides(false);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rides, responseEntity.getBody());
        verify(rideService).getAllRides(false);
    }

    @Test
    void getRideByIdTest_ReturnRide() {
        //given
        long rideId = 1;
        Ride ride = defaultRide();
        doReturn(ride).when(rideService).getRideById(rideId);

        //when
        ResponseEntity<Ride> responseEntity = rideController.getRideById(rideId);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ride, responseEntity.getBody());
        verify(rideService).getRideById(rideId);
    }

    @Test
    void getAllRidesByPassengerIdTest_ReturnsRides() {
        //given
        String passengerId = "1";
        List<Ride> rides = defaultRides(List.of(1L, 1L), List.of(3L, 4L));
        doReturn(rides).when(rideService).getAllRidesByPassengerId(passengerId);

        //when
        ResponseEntity<List<Ride>> responseEntity = rideController.getAllRidesByPassengerId(passengerId);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rides, responseEntity.getBody());
        verify(rideService).getAllRidesByPassengerId(passengerId);
        assertTrue(responseEntity.getBody().stream()
                .allMatch(ride -> ride.getPassengerId().equals(passengerId)));
    }

    @Test
    void getAllRidesByDriverIdTest_ReturnsRides() {
        //given
        long driverId = 1;
        List<Ride> rides = defaultRides(List.of(1L, 2L), List.of(1L, 1L));
        doReturn(rides).when(rideService).getAllRidesByDriverId(driverId);

        //when
        ResponseEntity<List<Ride>> responseEntity = rideController.getAllRidesByDriverId(driverId);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rides, responseEntity.getBody());
        verify(rideService).getAllRidesByDriverId(driverId);
        assertTrue(responseEntity.getBody().stream()
                .allMatch(ride -> ride.getDriverId() == driverId));
    }

    @Test
    void saveRideTest_ReturnsSavedRide() {
        RideDto rideDto = new RideDto("1", 1L, "Nezavisimosty 1",
                "Nezavisimosty 2", LocalDateTime.of(2024, 10, 3, 12, 0,
                0, 0), 100F, Currency.BYN);
        Ride savedRide = rideMapper.fromRideDtoToRide(rideDto);
        savedRide.setId(1);
        savedRide.setPassengerId("1");
        savedRide.setDriverId(1L);
        doReturn(savedRide).when(rideService).saveRide(rideDto.passengerId(), rideDto.driverId(),
                rideMapper.fromRideDtoToRide(rideDto));

        //when
        ResponseEntity<Ride> responseEntity = rideController.saveRide(rideDto);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(savedRide, responseEntity.getBody());
        verify(rideService).saveRide("1", 1L, rideMapper.fromRideDtoToRide(rideDto));
    }

    @Test
    void updateRideByIdTest_ReturnsUpdatedRide() {
        //given
        long rideId = 1;
        RidePutDto ridePutDto = new RidePutDto("1", 1L, "Nezavisimosty 1",
                "Nezavisimosty 2", RideStatus.ACCEPTED,
                LocalDateTime.of(2024, 10, 3, 12, 0,
                0, 0), 100F, Currency.BYN);
        Ride updatingRide = rideMapper.fromRidePutDtoToRide(ridePutDto);
        updatingRide.setId(1);
        updatingRide.setPassengerId("1");
        updatingRide.setDriverId(1L);
        doReturn(updatingRide).when(rideService).updateRide(rideId,
                ridePutDto.passengerId(), ridePutDto.driverId(),
                rideMapper.fromRidePutDtoToRide(ridePutDto));

        //when
        ResponseEntity<Ride> responseEntity = rideController.updateRideById(rideId, ridePutDto);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatingRide, responseEntity.getBody());
        verify(rideService).updateRide(rideId,
                ridePutDto.passengerId(), ridePutDto.driverId(),
                rideMapper.fromRidePutDtoToRide(ridePutDto));
    }

    @Test
    void patchRideByIdTest_ReturnsUpdatedRide() {
        //given
        long rideId = 1;
        RidePatchDto ridePatchDto = new RidePatchDto("1", 1L, "Nezavisimosty 1",
                "Nezavisimosty 2", RideStatus.ACCEPTED,
                LocalDateTime.of(2024, 10, 3, 12, 0,
                        0, 0), 100F, Currency.BYN);
        Ride updatingRide = rideMapper.fromRidePatchDtoToRide(ridePatchDto);
        updatingRide.setId(1);
        updatingRide.setPassengerId("1");
        updatingRide.setDriverId(1L);
        doReturn(updatingRide).when(rideService).patchRide(rideId,
                ridePatchDto.passengerId(), ridePatchDto.driverId(),
                rideMapper.fromRidePatchDtoToRide(ridePatchDto));

        //when
        ResponseEntity<Ride> responseEntity = rideController.patchRideById(rideId, ridePatchDto);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatingRide, responseEntity.getBody());
        verify(rideService).patchRide(rideId,
                ridePatchDto.passengerId(), ridePatchDto.driverId(),
                rideMapper.fromRidePatchDtoToRide(ridePatchDto));
    }

    @Test
    void updateRideStatusByIdTest_ReturnsRide() {
        //given
        long rideId = 1;
        RideStatus rideStatus = RideStatus.ACCEPTED;
        Ride ride = defaultRide();
        ride.setRideStatus(rideStatus);
        doReturn(ride).when(rideService).changeRideStatusById(rideId, rideStatus);

        //when
        ResponseEntity<Ride> responseEntity = rideController.updateRideStatusById(rideId, rideStatus);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ride, responseEntity.getBody());
        verify(rideService).changeRideStatusById(rideId, rideStatus);
    }

    @Test
    void deleteRideByIdTest_ReturnsVoid() {
        //given
        long rideId = 1;
        doNothing().when(rideService).deleteRideById(rideId);
        String result = "Ride with id " + rideId + " was successfully deleted.";

        //when
        ResponseEntity<String> responseEntity = rideController.deleteRideById(rideId);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(result, responseEntity.getBody());
        verify(rideService).deleteRideById(rideId);
    }

    @Test
    void deleteRideByPassengerIdTest_ReturnsVoid() {
        //given
        String passengerId = "1";
        doNothing().when(rideService).deleteRideByPassengerId(passengerId);
        String result = "Rides with passenger id " + passengerId + " was successfully deleted.";

        //when
        ResponseEntity<String> responseEntity = rideController.deleteRideByPassengerId(passengerId);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(result, responseEntity.getBody());
        verify(rideService).deleteRideByPassengerId(passengerId);
    }

    @Test
    void deleteRideByDriverIdTest_ReturnsVoid() {
        //given
        long driverId = 1;
        doNothing().when(rideService).deleteRideByDriverId(driverId);
        String result = "Rides with driver id " + driverId + " was successfully deleted.";

        //when
        ResponseEntity<String> responseEntity = rideController.deleteRideByDriverId(driverId);

        //then
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(result, responseEntity.getBody());
        verify(rideService).deleteRideByDriverId(driverId);
    }
}