package com.horn.energy_blockchain.Service.impl;/*
 *@Author: horn
 *@DATE: 2022/8/7 0007 23:08
 *@Description
 *@Version 1.0
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.horn.energy_blockchain.DTO.Page;
import com.horn.energy_blockchain.Entity.User;
import com.horn.energy_blockchain.Mapper.UserMapper;
import com.horn.energy_blockchain.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询
     **/
    @Override
    public IPage<User> selectUserPage(Page queryDTO) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page=new com.baomidou.mybatisplus.extension.plugins.pagination.Page(queryDTO.getPageNo(),queryDTO.getPageSize());

        return null;
    }

    @Override
    public Integer addUser(User user) {
        try{
            if (StringUtils.isEmpty(user.getLoginName())) {
               return 0;
            }
            if (StringUtils.isEmpty(user.getPassword())) {
                return 0;
            }
            userMapper.insert(user);
            return 1;
        }catch (Exception e){
            return 0;
        }

    }

    @Override
    public Integer updateUser(User user) {
        try {
            return userMapper.updateByPrimaryKey(user);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Integer deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public User verifyUser(String name) {
        System.out.println(name);
        return userMapper.selectByName(name);
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        try{
            for(Integer id:ids){
                userMapper.deleteByPrimaryKey(id);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
