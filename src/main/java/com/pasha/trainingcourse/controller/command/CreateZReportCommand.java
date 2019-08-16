package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.exception.ZReportAlreadyCreatedException;
import com.pasha.trainingcourse.model.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CreateZReportCommand implements Command {
    private ReportService reportService;

    CreateZReportCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            HttpSession session = request.getSession();
            reportService.createZReport((User) session.getAttribute("user"));
            return "redirect:/report";
        } catch (ZReportAlreadyCreatedException e){
            return "redirect:/report?message="+e.getMessage();
        }
    }
}