package com.software.modsen.ridesmicroservice.controllers;

import com.software.modsen.ridesmicroservice.entities.ride.*;
import com.software.modsen.ridesmicroservice.mappers.RideMapper;
import com.software.modsen.ridesmicroservice.services.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/rides", produces = "application/json")
@AllArgsConstructor
@Tag(name = "Ride controller", description = "Allows to interact with passengers.")
public class RideController {
    private RideService rideService;
    private final RideMapper RIDE_MAPPER = RideMapper.INSTANCE;

    @GetMapping
    @Operation(
            description = "Allows to get all rides."
    )
    public ResponseEntity<List<Ride>> getAllRides(
            @RequestParam(name = "includeCancelledAndCompleted",
                    required = false, defaultValue = "true")
            boolean includeCancelledAndCompleted
    ) {
        return ResponseEntity.ok(rideService.getAllRides(includeCancelledAndCompleted));
    }

    @GetMapping("/{id}")
    @Operation(
            description = "Allows to get ride by id."
    )
    public ResponseEntity<Ride> getRideById(@PathVariable("id") @Parameter(description = "Ride id.")
                                                long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @GetMapping("/passengers/{id}")
    @Operation(
            description = "Allows to get all rides by passenger id."
    )
    public ResponseEntity<List<Ride>> getAllRidesByPassengerId(
            @PathVariable("id")
            @Parameter(description = "Passenger id.")
            String passengerId) {
        return ResponseEntity.ok(rideService.getAllRidesByPassengerId(passengerId));
    }

    @GetMapping("/drivers/{id}")
    @Operation(
            description = "Allows to get all rides by driver id."
    )
    public ResponseEntity<List<Ride>> getAllRidesByDriverId(
            @PathVariable("id")
            @Parameter(description = "Driver id.")
            long driverId) {
        return ResponseEntity.ok(rideService.getAllRidesByDriverId(driverId));
    }

    @PostMapping
    @Operation(
            description = "Allows to save new ride."
    )
    public ResponseEntity<Ride> saveRide(
            @Valid
            @RequestBody
            @Parameter(description = "Ride entity.")
            RideDto rideDto) {
        return ResponseEntity.ok(rideService.saveRide(
                rideDto.passengerId(),
                rideDto.driverId(),
                RIDE_MAPPER.fromRideDtoToRide(rideDto)));
    }

    @PutMapping("/{id}")
    @Operation(
            description = "Allows to update ride by id."
    )
    public ResponseEntity<Ride> updateRideById(
            @PathVariable("id")
            @Parameter(description = "Ride id.")
            long id,

            @Valid
            @RequestBody
            @Parameter(description = "Ride entity.")
            RidePutDto ridePutDto) {
        return ResponseEntity.ok(rideService.updateRide(
                id,
                ridePutDto.passengerId(),
                ridePutDto.driverId(),
                RIDE_MAPPER.fromRidePutDtoToRide(ridePutDto)));
    }

    @PatchMapping("/{id}")
    @Operation(
            description = "Allows to update ride by id."
    )
    public ResponseEntity<Ride> patchRideById(
            @PathVariable("id")
            @Parameter(description = "Ride id.")
            long id,
            @Valid
            @RequestBody
            @Parameter(description = "Ride entity.")
            RidePatchDto ridePatchDto) {
        return ResponseEntity.ok(rideService.patchRide(
                id,
                ridePatchDto.passengerId(),
                ridePatchDto.driverId(),
                RIDE_MAPPER.fromRidePatchDtoToRide(ridePatchDto)));
    }

    @PatchMapping("/{id}/status")
    @Operation(
            description = "Allows to update ride status by id."
    )
    public ResponseEntity<Ride> updateRideStatusById(
            @PathVariable("id")
            @Parameter(description = "Ride id.")
            long id,
            @RequestParam(value = "rideStatus")
            @Parameter(description = "RIde status.")
            RideStatus rideStatus) {
        return ResponseEntity.ok(rideService.changeRideStatusById(id, rideStatus));
    }

    @DeleteMapping("/{id}")
    @Operation(
            description = "Allows to delete ride by id."
    )
    public ResponseEntity<String> deleteRideById(
            @PathVariable("id")
            @Parameter(description = "Ride id.")
            long id) {
        rideService.deleteRideById(id);
        return ResponseEntity.ok("Ride with id " + id + " was successfully deleted.");
    }

    @DeleteMapping("/passengers/{passenger_id}")
    @Operation(
            description = "Allows to delete ride by passenger id."
    )
    public ResponseEntity<String> deleteRideByPassengerId(
            @PathVariable("passenger_id")
            @Parameter(description = "Passenger id.")
            String passengerId) {
        rideService.deleteRideByPassengerId(passengerId);
        return ResponseEntity.ok("Rides with passenger id " + passengerId + " was successfully deleted.");
    }

    @DeleteMapping("/drivers/{driver_id}")
    @Operation(
            description = "Allows to delete ride by driver id."
    )
    public ResponseEntity<String> deleteRideByDriverId(
            @PathVariable("driver_id")
            @Parameter(description = "Driver id.")
            long driverId) {
        rideService.deleteRideByDriverId(driverId);
        return ResponseEntity.ok("Rides with driver id " + driverId + " was successfully deleted.");
    }
}