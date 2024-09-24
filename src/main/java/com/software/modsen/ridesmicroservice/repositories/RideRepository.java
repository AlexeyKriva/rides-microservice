package com.software.modsen.ridesmicroservice.repositories;

import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByPassengerId(long passengerId);
    List<Ride> findAllByDriverId(long passengerId);
    @Query("SELECT r FROM Ride r WHERE r.id = :id AND r.rideStatus NOT IN (:completedStatus, :cancelledStatus)")
    Optional<Ride> findByIdAndRideStatus(
            @Param("id") long id,
            @Param("completedStatus") RideStatus completedStatus,
            @Param("cancelledStatus") RideStatus cancelledStatus);
}
