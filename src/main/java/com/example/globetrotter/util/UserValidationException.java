package com.example.globetrotter.util;

// User validation fails
public class UserValidationException extends Exception {
    public UserValidationException(String message) {
        super(message);
    }

    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

