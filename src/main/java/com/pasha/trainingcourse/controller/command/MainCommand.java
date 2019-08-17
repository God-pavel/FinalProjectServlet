package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class MainCommand implements Command {
    private CheckService checkService;

    MainCommand(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String mes = request.getParameter("message");
        if (mes != null) {
            request.setAttribute("message", mes);
        }
        int page = 0;


        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                return "/WEB-INF/error/404.jsp";
            }
        }


        long numberOfChecks = checkService.getNumberOfChecks();
        long totalPages = (long) Math.ceil((double) numberOfChecks / 6);

        List<Check> pageChecks = new ArrayList<>();

        checkService.getAllChecks().forEach(check -> System.out.println(check.getId()));

        System.out.println("--------------------------------");
        try {
            pageChecks = checkService.getAllChecks().subList(page * 6, (page + 1) * 6);
        } catch (IndexOutOfBoundsException e) {
            if (numberOfChecks!=0) {
                pageChecks = checkService.getAllChecks().subList(page * 6, (int)(page*6+numberOfChecks%6));
            }
            else totalPages = 1;
        }

        System.out.println(numberOfChecks +", " + totalPages);

        pageChecks.forEach(check -> System.out.println(check.getId()));

        request.setAttribute("checks", pageChecks);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", 6);
        request.setAttribute("totalPages", totalPages);


        return "/main.jsp";
    }
}