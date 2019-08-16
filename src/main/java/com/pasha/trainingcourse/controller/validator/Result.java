package com.pasha.trainingcourse.controller.validator;

public class Result {
    private boolean valid;
    private String message;

    public Result(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public Result(boolean valid) {
        this.valid = valid;
        this.message = "";
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

}