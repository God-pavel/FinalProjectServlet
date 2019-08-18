package com.pasha.trainingcourse.model.dao.mapper;

import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.Report;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.entity.enums.ReportType;
import com.pasha.trainingcourse.model.service.CheckService;
import com.pasha.trainingcourse.model.service.UserService;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportMapper implements ObjectMapper<Report> {

    private final UserService userService;
    private final CheckService checkService;

    public ReportMapper(UserService userService, CheckService checkService) {
        this.userService = userService;
        this.checkService = checkService;
    }

    @Override
    public Report extractFromResultSet(ResultSet rs) throws SQLException {
        List<Check> checks = new ArrayList<>();
        Long id = rs.getLong("id");
        LocalDate date = rs.getDate("date").toLocalDate();
        BigDecimal total = rs.getBigDecimal("total");
        User user = userService.getUserById(rs.getLong("user_id"));
        ReportType type = ReportType.valueOf(rs.getString("type"));
        long checkId = rs.getLong("check_id");
        if (checkId != 0) {
            checks.add(checkService.getCheckById(checkId));
        }
        return new Report(id, date, total, user, checks, type);
    }

    private void completeChecks(Report report, ResultSet rs) throws SQLException {
        report.getChecks().add(checkService.getCheckById(rs.getLong("check_id")));
    }

    public Report extractFromRsWithALLChecks(ResultSet rs) throws SQLException {
        Report report = extractFromResultSet(rs);
        while (rs.next()) {
            completeChecks(report, rs);
        }
        return report;

    }

    @Override
    public void makeUnique(Map<Long, Report> cache,
                           Report report, ResultSet rs) throws SQLException {
        if (cache.keySet().contains(report.getId())) {
            cache.get(report.getId()).getChecks().add(checkService.getCheckById(rs.getLong("check_id")));
        } else {
            cache.put(report.getId(), report);
        }

    }
}