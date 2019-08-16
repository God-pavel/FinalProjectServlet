package com.pasha.trainingcourse.model.dao.impl;

import com.pasha.trainingcourse.model.dao.CheckDao;
import com.pasha.trainingcourse.model.dao.mapper.AbstractCheckMapper;
import com.pasha.trainingcourse.model.dao.mapper.CheckMapper;
import com.pasha.trainingcourse.model.dao.mapper.TempCheckMapper;
import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.service.ProductService;
import com.pasha.trainingcourse.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCCheckDao implements CheckDao {
    private Connection connection;
    private static final Logger log = LogManager.getLogger();
    private static final String INSERT_INTO_PRODUCT_IN_CHECK = "insert into product_in_check(check_id, product_id, amount) values (?, ?, ?)";
    private static final String INSERT_INTO_CHECKS = "insert into checks(time, total, user_id, to_delete) values (?, ?, ?, ?)";
    private static final String SELECT_LAST_CHECK = "select * from checks c left join product_in_check pic on c.id = pic.check_id order by id desc limit 1";
    private static final String INSERT_INTO_TEMP_CHECK = "insert into temp_check(time, total, user) values (?, ?, ?)";
    private static final String SELECT_CHECK_BY_ID = "select * from checks c left join product_in_check pic on c.id = pic.check_id where id =?";
    private static final String SELECT_TEMP_CHECK_BY_ID = "select * from temp_check tc left join product_in_temp_check pitc on tc.id = pitc.temp_check_id where id =?";
    private static final String SELECT_LAST_TEMP = "select * from temp_check tc left join product_in_temp_check pitc on tc.id = pitc.temp_check_id order by id desc limit 1";
    private static final String SELECT_ALL_FROM_CHECKS = "select * from checks c left join product_in_check pic on c.id = pic.check_id";
    private static final String SELECT_ALL_FROM_TEMP_CHECKS = "select * from temp_check tc left join product_in_temp_check pitc on tc.id = pitc.temp_check_id";
    private static final String DELETE_FROM_PRODUCT_IN_CHECK_BY_ID = "delete from product_in_check where check_id=?;";
    private static final String UPDATE_CHECKS = "update checks set total=?, to_delete=? where id=?;";
    private static final String DELETE_FROM_PRODUCT_IN_TEMP_CHECK_BY_ID = "delete from product_in_temp_check where temp_check_id=?;";
    private static final String UPDATE_TEMP_CHECK = "update temp_check set total=? where id=?;";
    private static final String INSERT_INTO_PRODUCT_IN_TEMP_CHECK = "insert into product_in_temp_check(temp_check_id, product_id, amount) values (?, ?, ?)";
    private static final String DELETE_FROM_PRODUCT_IN_CHECK = "delete from product_in_check where check_id=?";
    private static final String DELETE_FROM_CHECK = "delete from checks where id=?";
    private static final String DELETE_FROM_PRODUCT_IN_TEMP_CHECK = "delete from product_in_temp_check where temp_check_id=?";
    private static final String DELETE_FROM_TEMP_CHECK = "delete from temp_check where id=?";

    JDBCCheckDao(Connection connection) {
        this.connection = connection;
    }

    private void insertProducts(Map<Product, Long> products, Long id, String query) {
        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {
            for (Product key : products.keySet()) {
                ps.setLong(1, id);
                ps.setLong(2, key.getId());
                ps.setLong(3, products.get(key));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void insertProductsToDb(Map<Product, Long> products, Long id) {
        insertProducts(products, id, INSERT_INTO_PRODUCT_IN_CHECK);
    }

    private void insertProductsToTempDb(Map<Product, Long> products, Long id) {
        insertProducts(products, id, INSERT_INTO_PRODUCT_IN_TEMP_CHECK);
    }

    @Override
    public void create(Check entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement(INSERT_INTO_CHECKS)) {
            Timestamp ts = Timestamp.valueOf(entity.getTime());
            ps.setTimestamp(1, ts);
            ps.setBigDecimal(2, entity.getTotal());
            ps.setLong(3, entity.getUser().getId());
            ps.setBoolean(4, entity.isToDelete());
            ps.executeUpdate();
            insertProductsToDb(entity.getProductAmount(), findLast().getId());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Check findLast() {
        return findLastCheck(SELECT_LAST_CHECK, new CheckMapper(new UserService(), new ProductService()));
    }

    @Override
    public Check findLastTemp() {
        return findLastCheck(SELECT_LAST_TEMP, new TempCheckMapper(new UserService(), new ProductService()));
    }

    private Check findLastCheck(String query, AbstractCheckMapper om) {
        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return om.extractFromRsWithALLProducts(rs);
            } else return null;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTemp(Check temp) {
        try (PreparedStatement ps =
                     connection.prepareStatement(INSERT_INTO_TEMP_CHECK)) {
            Timestamp ts = Timestamp.valueOf(temp.getTime());
            ps.setTimestamp(1, ts);
            ps.setBigDecimal(2, temp.getTotal());
            ps.setString(3, temp.getUser().getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Check findById(Long id) {
        CheckMapper checkMapper = new CheckMapper(new UserService(), new ProductService());
        return findCheckById(id, SELECT_CHECK_BY_ID, checkMapper);
    }

    @Override
    public Check findTempById(Long id) {
        TempCheckMapper checkMapper = new TempCheckMapper(new UserService(), new ProductService());
        return findCheckById(id, SELECT_TEMP_CHECK_BY_ID, checkMapper);
    }

    private Check findCheckById(Long id, String query, AbstractCheckMapper om) {
        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return om.extractFromRsWithALLProducts(rs);
            } else return null;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Check> findAll() {
        CheckMapper checkMapper = new CheckMapper(new UserService(), new ProductService());
        return findAllCheck(SELECT_ALL_FROM_CHECKS, checkMapper);
    }

    @Override
    public List<Check> findAllTemp() {
        TempCheckMapper checkMapper = new TempCheckMapper(new UserService(), new ProductService());
        return findAllCheck(SELECT_ALL_FROM_TEMP_CHECKS, checkMapper);
    }

    private List<Check> findAllCheck(String query, AbstractCheckMapper om) {
        Map<Long, Check> checks = new HashMap<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Check check = om
                        .extractFromResultSet(rs);
                om.makeUnique(checks, check, rs);
            }
            return new ArrayList<>(checks.values());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public void update(Check entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement(DELETE_FROM_PRODUCT_IN_CHECK_BY_ID)) {
            ps.setLong(1, entity.getId());
            ps.execute();
            insertProductsToDb(entity.getProductAmount(), entity.getId());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps =
                     connection.prepareStatement(UPDATE_CHECKS)) {
            ps.setBigDecimal(1, entity.getTotal());
            ps.setBoolean(2, entity.isToDelete());
            ps.setLong(3, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTemp(Check entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement(DELETE_FROM_PRODUCT_IN_TEMP_CHECK_BY_ID)) {
            ps.setLong(1, entity.getId());
            ps.execute();
            insertProductsToTempDb(entity.getProductAmount(), entity.getId());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps =
                     connection.prepareStatement(UPDATE_TEMP_CHECK)) {
            ps.setBigDecimal(1, entity.getTotal());
            ps.setLong(2, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        deleteCheck(id, DELETE_FROM_PRODUCT_IN_CHECK, DELETE_FROM_CHECK);
    }

    @Override
    public void deleteTemp(Long id) {
        deleteCheck(id, DELETE_FROM_PRODUCT_IN_TEMP_CHECK, DELETE_FROM_TEMP_CHECK);
    }

    private void deleteCheck(Long id, String firstQuery, String secondQuery) {
        deleteHelper(id, firstQuery);
        deleteHelper(id, secondQuery);
    }

    private void deleteHelper(Long id, String query) {
        try (PreparedStatement ps =
                     connection.prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}