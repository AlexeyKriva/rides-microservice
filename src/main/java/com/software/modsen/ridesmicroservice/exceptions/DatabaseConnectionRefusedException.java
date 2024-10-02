package com.software.modsen.ridesmicroservice.exceptions;

public class DatabaseConnectionRefusedException extends RuntimeException {
    public DatabaseConnectionRefusedException(String message) {
        super(message);
    }
}