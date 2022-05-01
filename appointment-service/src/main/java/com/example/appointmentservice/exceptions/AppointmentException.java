package com.example.appointmentservice.exceptions;

import org.springframework.http.HttpStatus;

public class AppointmentException extends RuntimeException{
    private HttpStatus status;

    public AppointmentException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public AppointmentException(HttpStatus status, Throwable cause) {
        super(cause);
        this.status = status;
    }
}