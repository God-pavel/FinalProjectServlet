package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.exception.CheckCantBeDeleted;
import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class DeleteProductFromCheckCommand implements Command {
    private CheckService checkService;

    DeleteProductFromCheckCommand(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        String toParse = request.getRequestURI().replaceAll(".*/deleteProduct/", "");
        String [] params = toParse.split("/");
        try {
            checkService.deleteProductFromCheck(Long.parseLong(params[0]),params[1]);
            return "redirect:/main";

        } catch (CheckCantBeDeleted e) {
            return "redirect:/main?message="+e.getMessage();
        }



    }
}
