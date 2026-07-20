package com.projectmate.main.exception;

public class TeamFullException extends RuntimeException {
    public TeamFullException(String message) {
        super(message);
    }
}
