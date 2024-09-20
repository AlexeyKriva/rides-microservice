package com.software.modsen.ridesmicroservice.mappers;

import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RideMapper {
    RideMapper INSTANCE = Mappers.getMapper(RideMapper.class);

    Ride fromRideDtoToRide(RideDto rideDto);
}
