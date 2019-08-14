package com.pasha.trainingcourse.model.service;

import com.pasha.trainingcourse.model.dao.DaoFactory;
import com.pasha.trainingcourse.model.dao.ProductDao;
import com.pasha.trainingcourse.model.entity.Product;
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

    public boolean createProduct(Product product) {

        try (ProductDao dao = daoFactory.createProductDao()) {
            Product userFromDb = dao.findByName(product.getName());
            if (userFromDb != null) {
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


}
