package com.software.modsen.ridesmicroservice.exceptions;

public class RideWasCompletedOrCancelled extends RuntimeException {
    public RideWasCompletedOrCancelled(String message) {
        super(message);
    }
}