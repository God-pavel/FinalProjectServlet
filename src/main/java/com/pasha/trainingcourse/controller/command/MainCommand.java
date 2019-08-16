package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;

public class MainCommand implements Command {
    private CheckService checkService;

    MainCommand(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String mes = request.getParameter("message");
        if(mes!=null){
            request.setAttribute("message",mes);
        }
        request.setAttribute("checks", checkService.getAllChecks());
        return "/main.jsp";
    }
}