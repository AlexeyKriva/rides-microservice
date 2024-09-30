package com.software.modsen.ridesmicroservice.controllers;

import com.software.modsen.ridesmicroservice.entities.ride.*;
import com.software.modsen.ridesmicroservice.mappers.RideMapper;
import com.software.modsen.ridesmicroservice.services.RideService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/ride", produces = "application/json")
@AllArgsConstructor
public class RideController {
    private RideService rideService;
    private final RideMapper RIDE_MAPPER = RideMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @GetMapping("")
    private ResponseEntity<List<Ride>> getAllNotCompletedOrCancelledRides() {
        return ResponseEntity.ok(rideService.getAllNotCompletedAndNotCancelledRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable("id") long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @GetMapping("/{id}/not-completed-and-cancelled")
    public ResponseEntity<Ride> getNotCompletedOrCancelledRidesById(@PathVariable("id") long id) {
        return ResponseEntity.ok(rideService.getNotCompletedAndNotCancelledRideById(id));
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<List<Ride>> getAllRidesByPassengerId(@PathVariable("id") long passengerId) {
        return ResponseEntity.ok(rideService.getAllRidesByPassengerId(passengerId));
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<List<Ride>> getAllRidesByDriverId(@PathVariable("id") long driverId) {
        return ResponseEntity.ok(rideService.getAllRidesByDriverId(driverId));
    }

    @PostMapping
    public ResponseEntity<Ride> saveRide(@Valid @RequestBody RideDto rideDto) {
        return ResponseEntity.ok(rideService.saveRide(
                rideDto.getPassengerId(),
                rideDto.getDriverId(),
                RIDE_MAPPER.fromRideDtoToRide(rideDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ride> updateRideById(@PathVariable("id") long id,
                                               @Valid @RequestBody RidePutDto ridePutDto) {
        return ResponseEntity.ok(rideService.updateRide(
                id,
                ridePutDto.getPassengerId(),
                ridePutDto.getDriverId(),
                RIDE_MAPPER.fromRidePutDtoToRide(ridePutDto)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ride> patchRideById(@PathVariable("id") long id,
                                              @Valid @RequestBody RidePatchDto ridePatchDto) {
        return ResponseEntity.ok(rideService.patchRide(
                id,
                ridePatchDto.getPassengerId(),
                ridePatchDto.getDriverId(),
                RIDE_MAPPER.fromRidePatchDtoToRide(ridePatchDto)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Ride> updateRideStatusById(@PathVariable("id") long id, @RequestParam(value = "status")
    RideStatus rideStatus) {
        return ResponseEntity.ok(rideService.changeRideStatusById(id, rideStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRideById(@PathVariable("id") long id) {
        rideService.deleteRideById(id);
        return ResponseEntity.ok("Ride with id " + id + " was successfully deleted.");
    }

    @DeleteMapping("/{passenger_id}")
    public ResponseEntity<String> deleteRideByPassengerId(@PathVariable("passenger_id") long passengerId) {
        rideService.deleteRideByPassengerId(passengerId);
        return ResponseEntity.ok("Rides with passenger id " + passengerId + " was successfully deleted.");
    }

    @DeleteMapping("/{driver_id}")
    public ResponseEntity<String> deleteRideByDriverId(@PathVariable("driver_id") long driverId) {
        rideService.deleteRideByDriverId(driverId);
        return ResponseEntity.ok("Rides with driver id " + driverId + " was successfully deleted.");
    }
}
