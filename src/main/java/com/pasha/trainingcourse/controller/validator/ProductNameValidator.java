package com.pasha.trainingcourse.controller.validator;

public class ProductNameValidator implements Validator<String> {
    private String regex = "^[a-zA-Z]{3,16}$";
    private String message = "Invalid product name";

    @Override
    public Result validate(String password) {
        if (password.matches(regex)) {
            return new Result(true);
        } else {
            return new Result(false, message);
        }
    }

}