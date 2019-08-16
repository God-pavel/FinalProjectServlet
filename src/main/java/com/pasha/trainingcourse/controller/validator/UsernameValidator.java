package com.pasha.trainingcourse.controller.validator;

public class UsernameValidator implements Validator<String> {
    private String regex = "^[a-zA-Z0-9._-]{3,16}$";
    private String message = "Invalid username";

    @Override
    public Result validate(String login) {
        if (login.matches(regex)) {
            return new Result(true);
        } else {
            return new Result(false, message);
        }
    }

}
