package com.software.modsen.ridesmicroservice.exceptions;

public class InvalidReportTypeException extends RuntimeException {
    public InvalidReportTypeException(String message) {
        super(message);
    }
}
