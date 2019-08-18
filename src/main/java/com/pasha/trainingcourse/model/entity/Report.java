package com.pasha.trainingcourse.model.entity;

import com.pasha.trainingcourse.model.entity.enums.ReportType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Report {

    private Long id;
    private LocalDate date;
    private BigDecimal total;
    private User user;
    private List<Check> checks;
    private ReportType reportType;

    public Report(Long id, LocalDate date, BigDecimal total, User user, List<Check> checks, ReportType reportType) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.user = user;
        this.checks = checks;
        this.reportType = reportType;
    }

    public Report(LocalDate date, BigDecimal total, User user, List<Check> checks, ReportType reportType) {
        this.date = date;
        this.total = total;
        this.user = user;
        this.checks = checks;
        this.reportType = reportType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public void setChecks(List<Check> checks) {
        this.checks = checks;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", date=" + date +
                ", total=" + total +
                ", user=" + user +
                ", checks=" + checks +
                ", reportType=" + reportType +
                '}';
    }
}