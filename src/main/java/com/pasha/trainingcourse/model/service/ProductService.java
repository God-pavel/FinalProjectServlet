package com.pasha.trainingcourse.model.service;

import com.pasha.trainingcourse.model.dao.DaoFactory;
import com.pasha.trainingcourse.model.dao.ProductDao;
import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.exception.NotEnoughProductsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class ProductService {
    private static final Logger log = LogManager.getLogger();
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public List<Product> getAllProducts() {
        try (ProductDao dao = daoFactory.createProductDao()) {
            return dao.findAll();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Product getProductById(Long id) {
        try (ProductDao productDao = daoFactory.createProductDao()) {
            return productDao.findById(id);
        } catch (Exception e) {
            log.warn("Cant get product!");

            return null;
        }
    }

    public Product getProductByName(String name) {
        try (ProductDao productDao = daoFactory.createProductDao()) {
            return productDao.findByName(name);
        } catch (Exception e) {
            log.warn("Cant get product!");

            return null;
        }
    }

    public boolean createProduct(Product product) {

        try (ProductDao dao = daoFactory.createProductDao()) {
            Product productFromDb = dao.findByName(product.getName());
            if (productFromDb != null) {
                log.warn("product not unique!");
                return false;
            }
            dao.create(product);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            e.getStackTrace();
            return false;
        }
    }

    public boolean updateProduct(Product product) {
        try (ProductDao productDao = daoFactory.createProductDao()) {
            productDao.update(product);
            return true;
        } catch (Exception e) {
            log.warn("Cant update product!");
            return false;
        }
    }

    boolean checkProductAvailabilityByName(String name, Long amount) {
        if (getProductByName(name) == null) {
            throw new IllegalArgumentException("There are no product with that name!");
        }
        return getProductByName(name).getAmount() >= amount;
    }

    boolean checkProductAvailabilityById(Long id, Long amount) {
        if (getProductById(id) == null) {
            throw new IllegalArgumentException("There are no product with that id!");
        }
        return getProductById(id).getAmount() >= amount;
    }

    void takeAway(Product product, Long amount) throws NotEnoughProductsException {
        product.setAmount(product.getAmount() - amount);
        if (product.getAmount() < 0) {
            throw new NotEnoughProductsException("Not enough products in storage!");
        }
        updateProduct(product);
        log.info("Was taked" + product.getName() + " " + amount
                + ".\n Left " + product.getAmount() + product.getName());
    }

    void takeBack(Product product, Long amount) throws IllegalArgumentException {
        product.setAmount(product.getAmount() + amount);
        updateProduct(product);
        log.info("Was returned" + product.getName() + " " + amount
                + ".\n Left " + product.getAmount() + product.getName());
    }

}
