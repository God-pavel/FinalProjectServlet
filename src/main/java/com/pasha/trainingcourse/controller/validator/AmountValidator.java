package com.pasha.trainingcourse.controller.validator;

public class AmountValidator implements Validator<Long> {
    private String message = "Invalid amount";

    @Override
    public Result validate(Long amount) {
        if (amount < 1 || amount > 1000000) {
            return new Result(false, message);
        } else {
            return new Result(true);
        }
    }

}