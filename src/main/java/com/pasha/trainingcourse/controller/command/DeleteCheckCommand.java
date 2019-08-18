package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.exception.CheckCantBeDeleted;
import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;

public class DeleteCheckCommand implements Command {
    private CheckService checkService;

    DeleteCheckCommand(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        String checkID = request.getRequestURI().replaceAll(".*/deleteCheck/", "");

        try {
            checkService.deleteCheckById(Long.parseLong(checkID));
            return "redirect:/main";

        } catch (CheckCantBeDeleted e) {

            return "redirect:/main?message=" + e.getMessage();
        }


    }
}
