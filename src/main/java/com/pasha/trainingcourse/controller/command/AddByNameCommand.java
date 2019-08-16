package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.controller.validator.AmountValidator;
import com.pasha.trainingcourse.controller.validator.ProductNameValidator;
import com.pasha.trainingcourse.controller.validator.Result;
import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.exception.NotEnoughProductsException;
import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;

public class AddByNameCommand implements Command {
    private CheckService checkService;

    AddByNameCommand(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        String checkID = request.getRequestURI().replaceAll(".*/addByName/", "");
        Check check = checkService.getTemporaryCheckById(Long.parseLong(checkID));
        request.setAttribute("check", check);

        String name = request.getParameter("name");
        Long amount = Long.parseLong(request.getParameter("amount"));

        Result checkAmount = new AmountValidator().validate(amount);

        if (!checkAmount.isValid()) {
            return "redirect:/createCheck/" + checkID + "?message="+checkAmount.getMessage();
        }

        try {
            checkService.addProductToCheckByName(check.getId(), name, amount);
            return "redirect:/createCheck/" + checkID;
        } catch (NotEnoughProductsException | IllegalArgumentException e) {
            return "redirect:/createCheck/" + checkID + "?message="+e.getMessage();
        }

    }
}