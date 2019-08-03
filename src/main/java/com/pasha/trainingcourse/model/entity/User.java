package com.pasha.trainingcourse.model.entity;

import com.pasha.trainingcourse.model.Role;

import java.util.Set;

public class User {

    private Long id;

    private String username;
    private String password;
    private Role role;

    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }

    public boolean isSeniorCashier() {
        return role.equals(Role.SENIOR_CASHIER);
    }

    public boolean isUser() {
        return role.equals(Role.USER);
    }


    public boolean isMerchandiser() {
        return role.equals(Role.MERCHANDISER);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}