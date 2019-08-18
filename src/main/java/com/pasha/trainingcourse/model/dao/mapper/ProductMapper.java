package com.pasha.trainingcourse.model.dao.mapper;

import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.entity.enums.ProductType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ProductMapper implements ObjectMapper<Product> {
    @Override
    public Product extractFromResultSet(ResultSet rs) throws SQLException {

        return new Product(rs.getLong("id"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getLong("amount"),
                ProductType.valueOf(rs.getString("type")));
    }

    @Override
    public void makeUnique(Map<Long, Product> cache,
                           Product product, ResultSet rs) {
        cache.putIfAbsent(product.getId(), product);
    }
}
