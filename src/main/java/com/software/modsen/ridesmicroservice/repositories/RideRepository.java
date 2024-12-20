package com.software.modsen.ridesmicroservice.repositories;

import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByPassengerId(String passengerId);

    List<Ride> findAllByDriverId(long passengerId);

    @Query("SELECT r FROM Ride r WHERE r.id = :id AND r.rideStatus NOT IN (:completedStatus, :cancelledStatus)")
    Optional<Ride> findByIdAndRideStatus(
            @Param("id") long id,
            @Param("completedStatus") RideStatus completedStatus,
            @Param("cancelledStatus") RideStatus cancelledStatus);

    void deleteAllByPassengerId(String passengerId);

    void deleteAllByDriverId(long driverId);

    List<Ride> findByOrderDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    List<Ride> findByOrderDateTimeBefore(LocalDateTime dateTime, Pageable pageable);

    List<Ride> findByOrderDateTimeAfter(LocalDateTime dateTime, Pageable pageable);
}