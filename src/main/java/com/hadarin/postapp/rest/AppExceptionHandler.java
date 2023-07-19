package com.hadarin.postapp.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 *This class helps to handle and process exceptions
 */
@ControllerAdvice
public class AppExceptionHandler {

    /**
     *
     * @param ex exception to the concrete type (Exception)
     * @return response body with error code and description
     */
    @ExceptionHandler
    public ResponseEntity<ClientErrorResponse> handleException (Exception ex) {
        ex.printStackTrace();
        ClientErrorResponse error = new ClientErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Oops. Something went wrong. Probably external services are unavailable.", System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *Exception handler that excepts incorrect data
     * @param ex exception to the concrete type (IllegalArgumentException)
     * @return response body with error code and description
     */
    @ExceptionHandler
    public ResponseEntity<ClientErrorResponse> handleException (IllegalArgumentException ex) {
        ex.printStackTrace();
        ClientErrorResponse error =
                new ClientErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ClientErrorResponse> handleException (HttpServerErrorException ex) {
        ex.printStackTrace();
        ClientErrorResponse error =
                new ClientErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler
    public ResponseEntity<ClientErrorResponse> handleException (MethodArgumentTypeMismatchException ex) {
        ex.printStackTrace();
        ClientErrorResponse error =
                new ClientErrorResponse(HttpStatus.BAD_REQUEST.value(), "Please enter a correct request", System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
