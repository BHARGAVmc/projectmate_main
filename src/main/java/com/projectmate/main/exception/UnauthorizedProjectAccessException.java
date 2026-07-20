package com.projectmate.main.exception;

public class UnauthorizedProjectAccessException extends RuntimeException {
    public UnauthorizedProjectAccessException(String message) {
        super(message);
    }
}
