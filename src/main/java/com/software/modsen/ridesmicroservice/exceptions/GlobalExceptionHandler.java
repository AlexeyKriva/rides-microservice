package com.software.modsen.ridesmicroservice.exceptions;

import feign.FeignException;
import jakarta.mail.MessagingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.software.modsen.ridesmicroservice.exceptions.ExceptionMessage.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RideNotFondException.class)
    public ResponseEntity<String> rideNotFondExceptionHandler(RideNotFondException exception) {
        return new ResponseEntity<>(RIDE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RideWasCompletedOrCancelled.class)
    public ResponseEntity<String> rideWasCompletedOrCancelledHandler(RideWasCompletedOrCancelled exception) {
        return new ResponseEntity<>(RIDE_WAS_COMPLETED_OR_CANCELLED, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException
                                                                                        exception) {
        return new ResponseEntity<>(exception.getMethod() + METHOD_NOT_SUPPORTED_MESSAGE, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException
                                                                                       exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException
                                                                                     exception) {
        return new ResponseEntity<>(String.format(INVALID_TYPE_FOR_PARAMETER_MESSAGE, exception.getName(),
                exception.getRequiredType().getSimpleName(), exception.getValue()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<String> jsonMappingExceptionHandler(JsonMappingException exception) {
        return new ResponseEntity<>(JSON_MAPPING_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
        return new ResponseEntity<>(DATA_INTEGRITY_VIOLENT_MESSAGE, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        return new ResponseEntity<>(REQUEST_RESOURCE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> httpMessageNotReadableExceptionMessage(HttpMessageNotReadableException
                                                                                 exception) {
        return new ResponseEntity<>(INVALID_JSON_FORMAT, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> feignExceptionHandler(FeignException exception) {
        return new ResponseEntity<>(FEIGN_CANNOT_CONNECT_MESSAGE + exception.contentUTF8(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseConnectionRefusedException.class)
    public ResponseEntity<String> pSQLExceptionHandler(DatabaseConnectionRefusedException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReportGenerateException.class)
    public ResponseEntity<String> reportGenerateExceptionHandler(ReportGenerateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidReportTypeException.class)
    public ResponseEntity<String> invalidReportTypeExceptionHandler(InvalidReportTypeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReportDateTypeException.class)
    public ResponseEntity<String> invalidReportDateTypeExceptionHandler(InvalidReportDateTypeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> messagingExceptionHandler(MessagingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}