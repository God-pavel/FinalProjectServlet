package com.pasha.trainingcourse.model.dao;

import com.pasha.trainingcourse.model.dao.impl.JDBCDaoFactory;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract ProductDao createProductDao();

    public abstract UserDao createUserDao();

    public abstract CheckDao createCheckDao();

    public abstract ReportDao createReportDao();

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    daoFactory = new JDBCDaoFactory();
                }
            }
        }
        return daoFactory;
    }

}