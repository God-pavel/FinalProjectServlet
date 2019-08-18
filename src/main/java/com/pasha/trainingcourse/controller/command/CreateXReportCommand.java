package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CreateXReportCommand implements Command {
    private ReportService reportService;

    CreateXReportCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        reportService.createXReport((User) session.getAttribute("user"));
        return "redirect:/report";
    }
}