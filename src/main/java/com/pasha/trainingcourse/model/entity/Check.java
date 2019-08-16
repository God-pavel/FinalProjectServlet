package com.pasha.trainingcourse.model.entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class Check {

    private Long id;

    private User user;

    private LocalDateTime time;

    private BigDecimal total;

    private boolean toDelete;

    private Map<Product, Long> productAmount;

    public Check(User user, LocalDateTime time, BigDecimal total, boolean toDelete, Map<Product, Long> productAmount) {
        this.user = user;
        this.time = time;
        this.total = total;
        this.toDelete = toDelete;
        this.productAmount = productAmount;
    }

    public Check(Long id, User user, LocalDateTime time, BigDecimal total, boolean toDelete, Map<Product, Long> productAmount) {
        this.id = id;
        this.user = user;
        this.time = time;
        this.total = total;
        this.toDelete = toDelete;
        this.productAmount = productAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public Map<Product, Long> getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Map<Product, Long> productAmount) {
        this.productAmount = productAmount;
    }

    @Override
    public String toString() {
        return "Check{" +
                "id=" + id +
                ", user=" + user +
                ", time=" + time +
                ", total=" + total +
                ", toDelete=" + toDelete +
                ", productAmount=" + productAmount +
                '}';
    }
}
