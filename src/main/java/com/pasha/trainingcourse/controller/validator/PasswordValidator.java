package com.pasha.trainingcourse.controller.validator;

public class PasswordValidator implements Validator<String> {
    private String regex = "^[a-zA-Z0-9._-]{3,16}$";
    private String message = "Invalid password";

    @Override
    public Result validate(String password) {
        if (password.matches(regex)) {
            return new Result(true);
        } else {
            return new Result(false, message);
        }
    }

}
