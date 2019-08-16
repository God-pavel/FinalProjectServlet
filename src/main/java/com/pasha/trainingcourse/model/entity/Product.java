package com.pasha.trainingcourse.model.entity;

import com.pasha.trainingcourse.model.entity.enums.ProductType;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long amount;
    private ProductType productType;

    public Product(Long id, String name, BigDecimal price, Long amount, ProductType productType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.productType = productType;
    }

    public Product(String name, BigDecimal price, Long amount, ProductType productType) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.productType = productType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                ", productType=" + productType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                productType == product.productType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, productType);
    }
}