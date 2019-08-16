package com.pasha.trainingcourse.model.dao.impl;

import com.pasha.trainingcourse.model.dao.CheckDao;
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


    JDBCCheckDao(Connection connection) {
        this.connection = connection;
    }

    private void insertProductsToDb(Map<Product, Long> products, Long id) {
        try (PreparedStatement ps =
                     connection.prepareStatement("insert into product_in_check(check_id, product_id, amount) values (?, ?, ?)")) {
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


    @Override
    public void create(Check entity) {
        try (PreparedStatement ps =
                     connection.prepareStatement("insert into checks(time, total, user_id, to_delete) values (?, ?, ?, ?)")) {
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
        CheckMapper checkMapper = new CheckMapper(new UserService(), new ProductService());
        try (PreparedStatement ps =
                     connection.prepareStatement("select * from checks c left join product_in_check pic on c.id = pic.check_id order by id desc limit 1")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return checkMapper.extractFromRsWithALLProducts(rs);
            } else return null;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createTemp(Check temp) {
        try (PreparedStatement ps =
                     connection.prepareStatement("insert into temp_check(time, total, user) values (?, ?, ?)")) {
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
        try (PreparedStatement ps =
                     connection.prepareStatement("select * from checks c left join product_in_check pic on c.id = pic.check_id where id =?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return checkMapper.extractFromRsWithALLProducts(rs);
            } else return null;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Check findTempById(Long id) {
        TempCheckMapper checkMapper = new TempCheckMapper(new UserService(), new ProductService());
        try (PreparedStatement ps =
                     connection.prepareStatement("select * from temp_check tc left join product_in_temp_check pitc on tc.id = pitc.temp_check_id where id =?")) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return checkMapper.extractFromRsWithALLProducts(rs);
            } else return null;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public Check findLastTemp() {
        TempCheckMapper checkMapper = new TempCheckMapper(new UserService(), new ProductService());
        try (PreparedStatement ps =
                     connection.prepareStatement("select * from temp_check tc left join product_in_temp_check pitc on tc.id = pitc.temp_check_id order by id desc limit 1")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return checkMapper.extractFromRsWithALLProducts(rs);
            } else return null;
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Check> findAll() {
        Map<Long, Check> checks = new HashMap<>();
        final String query = "select * from checks c left join product_in_check pic on c.id = pic.check_id";
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            CheckMapper checkMapper = new CheckMapper(new UserService(), new ProductService());
            while (rs.next()) {
                Check check = checkMapper
                        .extractFromResultSet(rs);
                checkMapper.makeUnique(checks, check, rs);
            }
            return new ArrayList<>(checks.values());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            return null;
        }

    }

    @Override
    public List<Check> findAllTemp() {
        Map<Long, Check> checks = new HashMap<>();
        final String query = "select * from temp_check tc left join product_in_temp_check pitc on tc.id = pitc.temp_check_id";
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            TempCheckMapper checkMapper = new TempCheckMapper(new UserService(), new ProductService());
            while (rs.next()) {
                Check check = checkMapper
                        .extractFromResultSet(rs);
                checkMapper.makeUnique(checks, check, rs);
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
                     connection.prepareStatement("delete from product_in_check where check_id=?;")) {
            ps.setLong(1, entity.getId());
            ps.execute();
            insertProductsToDb(entity.getProductAmount(), entity.getId());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps =
                     connection.prepareStatement("update checks set total=?, to_delete=? where id=?;")) {
            ps.setBigDecimal(1, entity.getTotal());
            ps.setBoolean(2,entity.isToDelete());
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
                     connection.prepareStatement("delete from product_in_temp_check where temp_check_id=?;")) {
            ps.setLong(1, entity.getId());
            ps.execute();
            insertProductsToTempDb(entity.getProductAmount(), entity.getId());
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps =
                     connection.prepareStatement("update temp_check set total=? where id=?;")) {
            ps.setBigDecimal(1, entity.getTotal());
            ps.setLong(2, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void insertProductsToTempDb(Map<Product, Long> products, Long id) {
        try (PreparedStatement ps =
                     connection.prepareStatement("insert into product_in_temp_check(temp_check_id, product_id, amount) values (?, ?, ?)")) {
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

    @Override
    public void delete(Long id) {
        try (PreparedStatement ps =
                     connection.prepareStatement("delete from product_in_check where check_id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps =
                     connection.prepareStatement("delete from checks where id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteTemp(Long id) {
        try (PreparedStatement ps =
                     connection.prepareStatement("delete from product_in_temp_check where temp_check_id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps =
                     connection.prepareStatement("delete from temp_check where id=?")) {
            ps.setLong(1, id);
            ps.execute();
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