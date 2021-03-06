package com.pasha.trainingcourse.model.dao;

import com.pasha.trainingcourse.model.entity.Check;

import java.util.List;

public interface CheckDao extends GenericDao<Check> {
    void createTemp(Check temp);

    Check findTempById(Long id);

    List<Check> findAllTemp();

    void deleteTemp(Long id);

    void updateTemp(Check check);

    Check findLastTemp();

    Check findLast();

}
