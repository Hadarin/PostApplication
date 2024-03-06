package com.hadarin.postapp.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is the body of the error response in case of incorrect data input.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

}
