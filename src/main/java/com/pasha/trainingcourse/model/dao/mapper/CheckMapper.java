package com.pasha.trainingcourse.model.dao.mapper;

import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.service.ProductService;
import com.pasha.trainingcourse.model.service.UserService;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CheckMapper implements ObjectMapper<Check> {

    private final UserService userService;
    private final ProductService productService;

    public CheckMapper(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public Check extractFromResultSet(ResultSet rs) throws SQLException {
        Map<Product, Long> products = new HashMap<>();
        Long id = rs.getLong("id");
        LocalDateTime time = rs.getTimestamp("time").toLocalDateTime();
        BigDecimal total = rs.getBigDecimal("total");
        User user = userService.getUserById(rs.getLong("user_id"));
        Boolean toDelete = rs.getBoolean("to_delete");
        Long productId = rs.getLong("product_id");
        Long amount = rs.getLong("amount");
        if (productId != 0 && amount != 0) {
            products.put(productService.getProductById(productId), amount);
        }
        return new Check(id, user, time, total, toDelete, products);
    }

    private void completeProducts(Check check, ResultSet rs) throws SQLException {
        check.getProductAmount().put(productService.getProductById(rs.getLong("product_id")),
                rs.getLong("amount"));
    }

    public Check extractFromRsWithALLProducts(ResultSet rs) throws SQLException {
        Check check = extractFromResultSet(rs);
        while (rs.next()) {
            completeProducts(check, rs);
        }
        return check;

    }

    @Override
    public void makeUnique(Map<Long, Check> cache,
                           Check check, ResultSet rs) throws SQLException {
        if (cache.keySet().contains(check.getId())) {

            cache.get(check.getId()).getProductAmount().put(productService.getProductById(rs.getLong("product_id")),
                    rs.getLong("amount"));
        } else {
            cache.put(check.getId(), check);
        }

    }
}