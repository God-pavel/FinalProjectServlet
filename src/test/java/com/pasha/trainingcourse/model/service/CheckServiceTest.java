package com.pasha.trainingcourse.model.service;

import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.entity.enums.ProductType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;


public class CheckServiceTest {


    private CheckService checkService;

    @Before
    public void init() {
        ProductService productService = mock(ProductService.class);

        checkService = new CheckService(productService);
    }

    @Test
    public void calcTotal() {

        Map<Product, Long> products = new HashMap<>();
        products.put(new Product("test1", new BigDecimal(10),10L, ProductType.QUANTITY),10L);
        products.put(new Product("test2", new BigDecimal(5),10L, ProductType.QUANTITY),5L);
        long required = 125L;
        Assert.assertEquals(required, checkService.calcTotal(products), 0.0001);
    }

}