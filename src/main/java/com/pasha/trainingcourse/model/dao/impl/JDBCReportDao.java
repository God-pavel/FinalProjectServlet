package com.pasha.trainingcourse.model.dao.impl;

import com.pasha.trainingcourse.model.dao.ReportDao;
import com.pasha.trainingcourse.model.dao.mapper.ReportMapper;
import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.Report;
import com.pasha.trainingcourse.model.service.CheckService;
import com.pasha.trainingcourse.model.service.ProductService;
import com.pasha.trainingcourse.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCReportDao implements ReportDao {
    private Connection connection;
    private static final Logger log = LogManager.getLogger();
    private static final String INSERT_INTO_CHECK_IN_REPORT = "insert into check_in_report(report_id, check_id) values (?, ?)";
    private static final String INSERT_INTO_REPORT = "insert into report(date, total, type, user_id) values (?, ?, ?, ?)";
    private static final String SELECT_LAST_REPORT = "select * from report r left join check_in_report cir on r.id = cir.report_id order by id desc limit 1";
    private static final String SELECT_REPORT_BY_DATE = "select * from report r left join check_in_report cir on r.id = cir.report_id where date = ?";
    private static final String SELECT_ALL_REPORT = "select * from report r left join check_in_report cir on r.id = cir.report_id";


    JDBCReportDao(Connection connection) {
        this.connection = connection;
    }

    private void insertChecksToDb(List<Check> checks, Long id) {
        try (PreparedStatement ps =
                     connection.prepareStatement(INSERT_INTO_CHECK_IN_REPORT)) {
            for (Check check : checks) {
                ps.setLong(1, id);
                ps.setLong(2, check.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(Report entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement(INSERT_INTO_REPORT)) {
            connection.setAutoCommit(false);
            ps.setDate(1, Date.valueOf(entity.getDate()));
            ps.setBigDecimal(2, entity.getTotal());
            ps.setString(3, entity.getReportType().name());
            ps.setLong(4, entity.getUser().getId());
            ps.executeUpdate();
            insertChecksToDb(entity.getChecks(), findLast().getId());
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            rollBackTransaction(e, log, connection);
        }

    }

    private Report findLast() {
        ReportMapper reportMapper = new ReportMapper(new UserService(), new CheckService(new ProductService()));
        try (PreparedStatement ps =
                     connection.prepareStatement(SELECT_LAST_REPORT)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return reportMapper.extractFromRsWithALLChecks(rs);
            } else return null;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Report findById(Long id) {
        return null;
    }

    @Override
    public List<Report> findByDate(LocalDate date) {
        Map<Long, Report> reports = new HashMap<>();
        ReportMapper reportMapper = new ReportMapper(new UserService(), new CheckService(new ProductService()));
        try (PreparedStatement ps =
                     connection.prepareStatement(SELECT_REPORT_BY_DATE)) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Report report = reportMapper
                        .extractFromResultSet(rs);
                reportMapper.makeUnique(reports, report, rs);
            }
            return new ArrayList<>(reports.values());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Report> findAll() {
        Map<Long, Report> reports = new HashMap<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(SELECT_ALL_REPORT);
            ReportMapper reportMapper = new ReportMapper(new UserService(), new CheckService(new ProductService()));
            while (rs.next()) {
                Report report = reportMapper
                        .extractFromResultSet(rs);
                reportMapper.makeUnique(reports, report, rs);
            }
            return new ArrayList<>(reports.values());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Report entity) {

    }


    @Override
    public void delete(Long id) {

    }


    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
