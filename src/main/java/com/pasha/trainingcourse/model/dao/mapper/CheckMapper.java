package com.pasha.trainingcourse.model.dao.mapper;

import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.ProductService;
import com.pasha.trainingcourse.model.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckMapper extends AbstractCheckMapper {

    private final UserService userService;

    public CheckMapper(UserService userService, ProductService productService) {
        super(productService);
        this.userService = userService;
    }

    @Override
    public Check extractFromResultSet(ResultSet rs) throws SQLException {
        User user = userService.getUserById(rs.getLong("user_id"));
        boolean toDelete = rs.getBoolean("to_delete");
        return extractorHelper(user, toDelete, rs);
    }
}