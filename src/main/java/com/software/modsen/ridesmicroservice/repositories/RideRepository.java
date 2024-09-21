package com.software.modsen.ridesmicroservice.repositories;

import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
}
