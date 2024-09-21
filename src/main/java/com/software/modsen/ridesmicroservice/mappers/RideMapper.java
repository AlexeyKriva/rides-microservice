package com.software.modsen.ridesmicroservice.mappers;

import com.software.modsen.ridesmicroservice.entities.ride.Ride;
import com.software.modsen.ridesmicroservice.entities.ride.RideDto;
import com.software.modsen.ridesmicroservice.entities.ride.RidePatchDto;
import com.software.modsen.ridesmicroservice.entities.ride.RidePutDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RideMapper {
    RideMapper INSTANCE = Mappers.getMapper(RideMapper.class);

    Ride fromRideDtoToRide(RideDto rideDto);

    Ride fromRidePutDtoToRide(RidePutDto ridePutDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRideFromRidePatchDto(RidePatchDto ridePatchDto, @MappingTarget Ride ride);
}
