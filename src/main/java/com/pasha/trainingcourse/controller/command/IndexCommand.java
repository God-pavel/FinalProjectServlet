package com.pasha.trainingcourse.controller.command;

import javax.servlet.http.HttpServletRequest;

public class IndexCommand implements Command {


    @Override
    public String execute(HttpServletRequest request) {

        return "/WEB-INF/pages/index.jsp";
    }
}