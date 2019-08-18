package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.service.CheckService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements Command {
    private CheckService checkService;

    public MainCommand(CheckService checkService) {
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
                return "/WEB-INF/pages/404.jsp";
            }
        }


        long numberOfChecks = checkService.getNumberOfChecks();
        long totalPages = (long) Math.ceil((double) numberOfChecks / 6);
        List <Check> allChecks= checkService.getAllChecks();
        Collections.reverse(allChecks);

        List<Check> pageChecks = new ArrayList<>();

        try {
            pageChecks = allChecks.subList(page * 6, (page + 1) * 6);
        } catch (IndexOutOfBoundsException e) {
            if (numberOfChecks != 0) {
                pageChecks = allChecks.subList(page * 6, (int) (page * 6 + numberOfChecks % 6));
            } else totalPages = 1;
        }

        request.setAttribute("checks", pageChecks);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", 6);
        request.setAttribute("totalPages", totalPages);


        return "/WEB-INF/pages/main.jsp";
    }
}