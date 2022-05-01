package com.example.userservice.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException{
    private HttpStatus status;

    public UserException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public UserException(HttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
    }
}