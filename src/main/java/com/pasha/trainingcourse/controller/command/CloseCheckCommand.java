package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.exception.NotEnoughProductsException;
import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;

public class CloseCheckCommand implements Command {
    private CheckService checkService;

    CloseCheckCommand(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        String checkID = request.getRequestURI().replaceAll(".*/closeCheck/", "");
        try {
            checkService.closeCheck(Long.parseLong(checkID));
        } catch (NotEnoughProductsException e) {
            request.setAttribute("message", "Not enough products in storage");
            return "redirect:/createCheck/" + checkID;
        }


        return "redirect:/main";
    }
}