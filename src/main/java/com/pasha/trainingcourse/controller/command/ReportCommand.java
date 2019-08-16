package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.ReportService;

import javax.servlet.http.HttpServletRequest;

public class ReportCommand implements Command {
    private ReportService reportService;

    ReportCommand(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String mes = request.getParameter("message");
        if(mes!=null){
            request.setAttribute("message",mes);
        }
        request.setAttribute("reports", reportService.getAllReports());
        return "/report.jsp";
    }
}