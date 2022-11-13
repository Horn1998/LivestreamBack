package com.horn.energy_blockchain.Controller;

import com.horn.energy_blockchain.DTO.Login;
import com.horn.energy_blockchain.Entity.User;
import com.horn.energy_blockchain.Service.ILoginService;
import com.horn.energy_blockchain.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private ILoginService loginService;

    @PostMapping(value = "/api/login")
    @CrossOrigin       //后端跨域
    public Result login(@RequestBody Login loginDTO){
        try {
            return loginService.login(loginDTO);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Result(400, "登录失败", "");
        }
    }

    @PostMapping( value = "/api/register")
    @CrossOrigin
    public Result register(@RequestBody User user){
        try{

            return loginService.register(user);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Result(400, "注册失败", "");
        }
    }

}
