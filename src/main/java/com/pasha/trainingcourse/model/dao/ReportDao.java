package com.pasha.trainingcourse.model.dao;

import com.pasha.trainingcourse.model.entity.Report;

import java.time.LocalDate;
import java.util.List;

public interface ReportDao extends GenericDao<Report> {
    List<Report> findByDate(LocalDate date);
}
