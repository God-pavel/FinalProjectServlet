package com.pasha.trainingcourse.model.service;

import com.pasha.trainingcourse.model.dao.DaoFactory;
import com.pasha.trainingcourse.model.dao.ReportDao;
import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.Report;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.entity.enums.ReportType;
import com.pasha.trainingcourse.model.exception.ZReportAlreadyCreatedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class realizing reports logic
 *
 * @version 1.0
 * @autor Pavlo Pankratov
 */

public class ReportService {

    /**
     * Logger field
     */
    private static final Logger log = LogManager.getLogger();
    /**
     * Dao factory instance field
     */
    private DaoFactory daoFactory = DaoFactory.getInstance();
    /**
     * Check service field
     */
    private final CheckService checkService;

    /**
     * Constructor - creating new object
     *
     * @param checkService - производитель
     */
    public ReportService(CheckService checkService) {
        this.checkService = checkService;
    }


    /**
     * Method to get all reports
     *
     * @return returns list of reports
     */
    public List<Report> getAllReports() {
        try (ReportDao dao = daoFactory.createReportDao()) {
            return dao.findAll();
        } catch (Exception e) {
            log.warn("Cant get reports!");
            return Collections.emptyList();
        }
    }

    /**
     * Method to get reports certain date
     *
     * @param date found date
     * @return returns list of reports
     */
    private List<Report> getReportsByDate(LocalDate date) {
        try (ReportDao dao = daoFactory.createReportDao()) {
            return dao.findByDate(date);
        } catch (Exception e) {
            log.warn("Cant get reports!");
            return Collections.emptyList();
        }
    }

    /**
     * Method to call dao creating report method
     */
    private void createReport(Report report) {
        try (ReportDao dao = daoFactory.createReportDao()) {
            dao.create(report);

        } catch (Exception e) {
            log.warn(e.getMessage());
            e.getStackTrace();

        }
    }

    /**
     * Method to get check total value
     *
     * @param check certain date
     * @return returns double total
     */
    private Double getCheckTotal(Check check) {
        return check.getTotal().doubleValue();
    }


    /**
     * Method to get all today checks
     *
     * @return returns set od checks
     */
    private Set<Check> getTodayChecks() {
        return checkService.getAllChecks().stream()
                .filter(check -> check.getTime().
                        getDayOfYear() == LocalDate.now().getDayOfYear())
                .collect(Collectors.toSet());
    }

    /**
     * Method to calculate report total value
     *
     * @param checks set of report checks
     * @return returns BigDecimal total
     */
    private BigDecimal calcTotalSum(Set<Check> checks) {
        List<Double> totals = checks.stream()
                .map(this::getCheckTotal)
                .collect(Collectors.toList());

        return new BigDecimal(totals.stream().reduce(0.0, (x, y) -> x + y))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Method to check if today was already created z-report
     *
     * @return returns boolean value
     */
    public boolean getTodayZReport() {
        List<Report> todayReports = getReportsByDate(LocalDate.now());
//        if(todayReports.isEmpty()){return false;}
        List<Report> todayZReports = todayReports.stream()
                .filter(report -> report.getReportType() == ReportType.ZReport)
                .collect(Collectors.toList());
        return !todayZReports.isEmpty();
    }

    /**
     * Method to create x-report
     *
     * @param user user that create report
     */
    public void createXReport(User user) {
        log.info("start create method");
        Set<Check> todayChecks = getTodayChecks();
        log.info("today checks: " + todayChecks);
        BigDecimal totalSum = calcTotalSum(todayChecks);
        log.info("total:  " + totalSum);

        Report report = new Report(LocalDate.now(), totalSum, user, new ArrayList<>(), ReportType.XReport);
        log.info("date:  " + LocalDate.now());
        finishReport(report, todayChecks);
        log.info("X-report was saved. Report id: " + report.getId());

    }

    /**
     * Method to create z-report
     *
     * @param user user that create report
     */
    public void createZReport(User user) {
        if (getTodayZReport()) {
            throw new ZReportAlreadyCreatedException("ZReport was already created today!");
        }
        checkService.deleteUncompletedChecks();
        Set<Check> todayChecks = getTodayChecks();
        BigDecimal totalSum = calcTotalSum(todayChecks);

        Report report = new Report(LocalDate.now(), totalSum, user, new ArrayList<>(), ReportType.ZReport);

        finishReport(report, todayChecks);

        log.info("Z-report was saved. Report id: " + report.getId());
    }

    /**
     * Method to calculate report total value
     *
     * @param report report needs to be finished
     * @param checks report checks
     */
    private void finishReport(Report report, Set<Check> checks) {
        log.info("in finish method");
        checks.forEach(check -> {
            log.info("check: " + check);
            check.setToDelete(false);
            log.info("check: " + check);
            checkService.updateCheck(check);
            report.getChecks().add(check);
        });
        log.info("report: " + report);
        createReport(report);
    }


}