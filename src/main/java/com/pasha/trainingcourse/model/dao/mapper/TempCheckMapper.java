package com.pasha.trainingcourse.model.dao.mapper;

import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.ProductService;
import com.pasha.trainingcourse.model.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TempCheckMapper extends AbstractCheckMapper {

    private final UserService userService;

    public TempCheckMapper(UserService userService, ProductService productService) {
        super(productService);
        this.userService = userService;
    }

    @Override
    public Check extractFromResultSet(ResultSet rs) throws SQLException {
        User user = userService.getUserByUsername(rs.getString("user"));
        return extractorHelper(user, true, rs);
    }
}