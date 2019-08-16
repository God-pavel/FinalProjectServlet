package com.pasha.trainingcourse.controller.validator;

public interface Validator<T> {

    Result validate(T value);
}
