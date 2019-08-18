package com.pasha.trainingcourse.controller.command;


import com.pasha.trainingcourse.controller.validator.AmountValidator;
import com.pasha.trainingcourse.controller.validator.Result;
import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.exception.NotEnoughProductsException;
import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;

public class AddByIdCommand implements Command {
    private CheckService checkService;

    AddByIdCommand(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        String checkID = request.getRequestURI().replaceAll(".*/addById/", "");
        Check check = checkService.getTemporaryCheckById(Long.parseLong(checkID));
        request.setAttribute("check", check);

        String id = request.getParameter("id");
        Long amount = Long.parseLong(request.getParameter("amount"));

        Result checkAmount = new AmountValidator().validate(amount);

        if (!checkAmount.isValid()) {
            request.setAttribute("message", checkAmount.getMessage());
            return "redirect:/createCheck/" + checkID + "?message=" + checkAmount.getMessage();
        }

        try {
            checkService.addProductToCheckById(check.getId(), Long.parseLong(id), amount);
            return "redirect:/createCheck/" + checkID;
        } catch (NotEnoughProductsException | IllegalArgumentException e) {
            return "redirect:/createCheck/" + checkID + "?message=" + e.getMessage();
        }

    }
}