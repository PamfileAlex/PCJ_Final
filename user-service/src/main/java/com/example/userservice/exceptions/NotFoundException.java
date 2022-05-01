package com.example.userservice.exceptions;

public class NotFoundException extends RuntimeException {
    public <T> NotFoundException(String id) {
        super("ID " + id + " does not exist!");
    }
}
