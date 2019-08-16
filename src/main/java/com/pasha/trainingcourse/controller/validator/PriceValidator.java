package com.pasha.trainingcourse.controller.validator;

import java.math.BigDecimal;

public class PriceValidator implements Validator<BigDecimal> {
    private String message = "Invalid price";

    @Override
    public Result validate(BigDecimal price) {
        if (price.compareTo(new BigDecimal(0)) > 0 && price.compareTo(new BigDecimal(1000000)) < 0) {
            return new Result(true);
        } else {
            return new Result(false, message);
        }
    }
}
