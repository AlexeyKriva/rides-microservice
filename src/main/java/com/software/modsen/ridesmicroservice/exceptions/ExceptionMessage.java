package com.software.modsen.ridesmicroservice.exceptions;

public class ExceptionMessage {
    public static final String RIDE_NOT_FOUND_MESSAGE = "Ride not found.";
    public static final String RIDE_WAS_COMPLETED_OR_CANCELLED = "Ride was completed or cancelled.";
    public static final String METHOD_NOT_SUPPORTED_MESSAGE = " method is not supported.";
    public static final String INVALID_TYPE_FOR_PARAMETER_MESSAGE = "Invalid value for parameter '%s'. Expected type:" +
            " %s, but got: %s.";
    public static final String JSON_MAPPING_MESSAGE = "Invalid data in JSON";
    public static final String REQUEST_RESOURCE_NOT_FOUND_MESSAGE = "The requested resource was not found. Please" +
            " check the URL and try again.";
    public static final String INVALID_JSON_FORMAT = "Invalid json format.";
    public static final String DATA_INTEGRITY_VIOLENT_MESSAGE = "Not all data entered.";
    public static final String FEIGN_CANNOT_CONNECT_MESSAGE = "Unsuccessful attempt to connect to the service for" +
            " passengers or drivers. ";

}
