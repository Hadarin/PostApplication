package com.hadarin.postapp.rest;

/**
 *This class is the body of the error response in case of incorrect data input.
 */
public class ClientErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

    public ClientErrorResponse() {
    }

    public ClientErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
