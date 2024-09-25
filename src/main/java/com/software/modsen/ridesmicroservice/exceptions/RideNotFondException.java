package com.software.modsen.ridesmicroservice.exceptions;

public class RideNotFondException extends RuntimeException {
    public RideNotFondException(String message) {
        super(message);
    }
}