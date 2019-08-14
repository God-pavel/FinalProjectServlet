package com.pasha.trainingcourse.model.dao.impl;

import com.pasha.trainingcourse.model.dao.ProductDao;
import com.pasha.trainingcourse.model.dao.mapper.ProductMapper;
import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.entity.enums.ProductType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCProductDao implements ProductDao {
    private Connection connection;
    private static final Logger log = LogManager.getLogger();


    JDBCProductDao(Connection connection) {
        this.connection = connection;
        if (findByName("Banana") == null) {
            create(new Product("banana", new BigDecimal(3.99), 100L, ProductType.QUANTITY));
        }

    }

    @Override
    public void create(Product entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement("insert into product(name, price, amount, type) values (?, ?, ?, ?)")) {
            ps.setString(1, entity.getName());
            ps.setBigDecimal(2, entity.getPrice());
            ps.setLong(3, entity.getAmount());
            ps.setString(4, entity.getProductType().name());
            ps.executeUpdate();
            log.info(entity.getName() + " created.");
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }

    }


    @Override
    public Product findByName(String name) {
        ProductMapper productMapper = new ProductMapper();
        try (PreparedStatement ps =
                     connection.prepareStatement("select * from product where name =?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return productMapper.extractFromResultSet(rs);
            }
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findById(Long id) {
        ProductMapper productMapper = new ProductMapper();

        try (PreparedStatement ps =
                     connection.prepareStatement("select * from product where id =?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return productMapper.extractFromResultSet(rs);
            }
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List<Product> findAll() {

        Map<Long, Product> products = new HashMap<>();

        final String query = "select * from product";
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            ProductMapper productMapper = new ProductMapper();
            while (rs.next()) {
                Product product = productMapper
                        .extractFromResultSet(rs);
                productMapper.makeUnique(products, product, rs);
            }
            return new ArrayList<>(products.values());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void update(Product entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement("update product set amount=?, price=? where id=?")) {
            ps.setLong(1, entity.getAmount());
            ps.setBigDecimal(2, entity.getPrice());
            ps.setLong(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
