package com.pasha.trainingcourse.model.dao;

import com.pasha.trainingcourse.model.entity.Product;

public interface ProductDao extends GenericDao<Product> {
    Product findByName(String name);
}
