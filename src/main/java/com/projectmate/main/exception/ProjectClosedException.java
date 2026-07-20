package com.projectmate.main.exception;

public class ProjectClosedException extends RuntimeException {
    public ProjectClosedException(String message) {
        super(message);
    }
}
