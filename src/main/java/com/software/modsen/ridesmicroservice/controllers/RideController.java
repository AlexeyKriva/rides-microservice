package com.software.modsen.ridesmicroservice.controllers;

import com.software.modsen.ridesmicroservice.entities.ride.*;
import com.software.modsen.ridesmicroservice.services.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/ride", produces = "application/json")
public class RideController {
    @Autowired
    private RideService rideService;

    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable("id") long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @PostMapping
    public ResponseEntity<Ride> saveRide(@RequestBody RideDto rideDto) {
        System.out.println(rideDto);
        return ResponseEntity.ok(rideService.saveRide(rideDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ride> updateRideById(@PathVariable("id") long id, @RequestBody RidePutDto ridePutDto) {
        return ResponseEntity.ok(rideService.updateRide(id, ridePutDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ride> patchRideById(@PathVariable("id") long id, @RequestBody RidePatchDto ridePatchDto) {
        return ResponseEntity.ok(rideService.patchRide(id, ridePatchDto));
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
}
