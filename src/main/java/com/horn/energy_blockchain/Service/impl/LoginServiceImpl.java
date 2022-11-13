package com.horn.energy_blockchain.Service.impl;

import com.horn.energy_blockchain.DTO.Login;
import com.horn.energy_blockchain.DTO.LoginVO;
import com.horn.energy_blockchain.Entity.User;
import com.horn.energy_blockchain.Mapper.UserMapper;
import com.horn.energy_blockchain.Service.ILoginService;
import com.horn.energy_blockchain.common.Result;
import com.horn.energy_blockchain.Utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.UUID;

@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(Login loginDTO) {
        String password = loginDTO.getPassWord();
        loginDTO.setPassWord(PasswordUtil.desEncrypt(password));
        if (StringUtils.isEmpty(loginDTO.getLoginName())) {
            return new Result(400, "账号不能为空", "");
        }
        if (StringUtils.isEmpty(loginDTO.getPassWord())) {
            return new Result(400, "密码不能为空", "");
        }
        //通过登录名查询用户
        User user = userMapper.selectByName(loginDTO.getLoginName());
//        User user = userMapper.selectByPrimaryKey(19);
        //对加密密码进行解密
        String targetP = user.getPassword();
        user.setPassword(PasswordUtil.desEncrypt(targetP));
        //比较密码
        if (user != null && user.getPassword().equals(loginDTO.getPassWord())) {
            LoginVO loginVO = new LoginVO();
            loginVO.setId(user.getId());
            //这里token直接用一个uuid
            //使用jwt的情况下，会生成一个jwt token,jwt token里会包含用户的信息
            loginVO.setToken(UUID.randomUUID().toString());
            loginVO.setUser(user);
            return new Result(200, "", loginVO);
        }
        return new Result(401, "登录失败", "");
    }

    @Override
    public Result register(User user) {
        try{
            if (StringUtils.isEmpty(user.getLoginName())) {
                return new Result(400, "账号不能为空", "");
            }
            if (StringUtils.isEmpty(user.getPassword())) {
                return new Result(400, "密码不能为空", "");
            }
            userMapper.insert(user);
            return new Result(200, "注册成功", "");
        }catch (Exception e){
            return new Result(400, e.getMessage(), "");
        }
    }
}

