package com.horn.energy_blockchain.DTO;/*
 *@Author: horn
 *@DATE: 2022/8/6 0006 15:00
 *@Description 登录VO
 *@Version 1.0
 */

import com.horn.energy_blockchain.Entity.User;

public class LoginVO {
    private Integer id;
    private String token;
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
