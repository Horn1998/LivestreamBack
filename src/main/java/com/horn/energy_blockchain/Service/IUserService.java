package com.horn.energy_blockchain.Service;/*
 *@Author: horn
 *@DATE: 2022/8/7 0007 23:08
 *@Description
 *@Version 1.0
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.horn.energy_blockchain.DTO.Page;
import com.horn.energy_blockchain.Entity.User;

import java.util.List;

public interface IUserService {

    public IPage<User> selectUserPage(Page page);

    public Integer addUser(User user);

    public Integer updateUser(User user);

    public Integer deleteUser(Integer id);

    public void batchDelete(List<Integer> ids);

    public User verifyUser(String name);
}
