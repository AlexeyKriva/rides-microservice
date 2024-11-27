package com.software.modsen.ridesmicroservice.exceptions;

public class InvalidReportDateTypeException extends RuntimeException {
    public InvalidReportDateTypeException(String message) {
        super(message);
    }
}
