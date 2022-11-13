package com.horn.energy_blockchain.Service.impl;/*
 *@Author: horn
 *@DATE: 2022/11/12 0012 20:32
 *@Description
 *@Version 1.0
 */

import com.horn.energy_blockchain.Entity.Sign;
import com.horn.energy_blockchain.Mapper.SignMapper;
import com.horn.energy_blockchain.Service.ISignService;
import com.horn.energy_blockchain.Utils.RandomString;
import io.netty.util.internal.ThreadLocalRandom;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SignServiceImpl implements ISignService {
    @Autowired
    SignMapper signMapper;
    @Override
    public boolean checkSign(String room, String name, String date) {
        try {
            //计算年，月，日
            String[] times = date.split("-");
            Integer day = Integer.parseInt(times[0]);
            String month = times[1] + "/" + times[2];
            //获取签到码
            String adminCode = signMapper.getAdminCode(room);
            //获取完整签到信息
            Sign sign = signMapper.getSign(adminCode, name);
            //判断当前月份用户是否签到
            if(sign != null){
                //更新签到code
                String code = sign.getSignCode();
                if(code.charAt(day-1) == '1') return true; //已经签到
                StringBuilder sb = new StringBuilder(code);
                sb.setCharAt(day-1, '1');
                sign.setSignCode(sb.toString());
                //total + 1
                sign.setTotal(sign.getTotal()+1);
                //更新签到信息
                signMapper.updateByPrimaryKey(sign);
            }else{
                sign = new Sign();
                //插入数据 total = 1
                sign.setTotal(1); sign.setName(name); sign.setMonth(month);
                String code = "";
                for(int i=0; i<31; ++i){
                    if(i == day-1) code += '1';
                    else code += '0';
                }
                sign.setSignCode(code);
                sign.setAdminCode(adminCode);
                sign.setRole(0);
                signMapper.insert(sign);
            }
            return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void signForAll(String room, String name, String date) {
        try {
            //判断是否已经发起了签到
            if(signMapper.checkRoom(room) == 1) return;
            //计算年，月，日
            String[] times = date.split("-");
            Integer day = Integer.parseInt(times[0]);
            String month = times[1] + "/" + times[2];
            Sign sign = new Sign();
            //插入数据 total = 1
            sign.setTotal(0);
            sign.setName(room); //存在room和用户重名的情况，可以通过code辅助辨认
            sign.setMonth(month);
            sign.setSignCode("");
            String adminCode = new RandomString(20, ThreadLocalRandom.current()).getString();
            sign.setAdminCode(adminCode);
            sign.setRole(1);
            signMapper.insert(sign);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkRoom(String room){
        if(signMapper.checkRoom(room) == 1) return true;
        return false;
    }

    @Override
    public void stopSign(String room) {
        signMapper.stopSign(room);
    }
}
