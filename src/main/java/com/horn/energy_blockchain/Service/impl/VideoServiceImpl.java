package com.horn.energy_blockchain.Service.impl;/*
 *@Author: horn
 *@DATE: 2022/9/6 0006 20:39
 *@Description
 *@Version 1.0
 */

import com.horn.energy_blockchain.Entity.User;
import com.horn.energy_blockchain.Service.IVideoService;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements IVideoService {
    @Override
    public boolean verify(User user) {
        return false;
    }
}
