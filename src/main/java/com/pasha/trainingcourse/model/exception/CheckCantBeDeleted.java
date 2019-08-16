package com.pasha.trainingcourse.model.exception;

public class CheckCantBeDeleted extends RuntimeException {
    public CheckCantBeDeleted(String message) {
        super(message);
    }
}

