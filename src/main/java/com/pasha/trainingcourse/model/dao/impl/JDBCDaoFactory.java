package com.pasha.trainingcourse.model.dao.impl;

import com.pasha.trainingcourse.model.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCDaoFactory extends DaoFactory {

    private DataSource dataSource = ConnectionPoolHolder.getDataSource();
    private static final Logger log = LogManager.getLogger();


    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(getConnection());
    }

    @Override
    public ProductDao createProductDao() {
        return new JDBCProductDao(getConnection());

    }

    @Override
    public ReportDao createReportDao() {
        return new JDBCReportDao(getConnection());

    }

    @Override
    public CheckDao createCheckDao() {
        return new JDBCCheckDao(getConnection());

    }

    private Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
}