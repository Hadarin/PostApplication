package com.hadarin.postapp.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *This class helps to handle and process exceptions
 */
@ControllerAdvice
public class AppExceptionHandler {

    /**
     *
     * @param ex exception of the concrete type (Exception)
     * @return response body with error code and description
     */
    @ExceptionHandler
    public ResponseEntity<ClientErrorResponse> handleException (Exception ex) {
        ex.printStackTrace();
        ClientErrorResponse error = new ClientErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Oops. Something went wrong.", System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *Exception hadler that excepts incorrect data
     * @param ex exception of the concrete type (IllegalArgumentException)
     * @return response body with error code and description
     */
    @ExceptionHandler
    public ResponseEntity<ClientErrorResponse> handleException (IllegalArgumentException ex) {
        ex.printStackTrace();
        ClientErrorResponse error =
                new ClientErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
