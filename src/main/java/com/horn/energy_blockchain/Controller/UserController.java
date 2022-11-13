package com.horn.energy_blockchain.Controller;/*
 *@Author: horn
 *@DATE: 2022/8/7 0007 23:06
 *@Description
 *@Version 1.0
 */

import com.horn.energy_blockchain.common.Result;
import com.horn.energy_blockchain.DTO.Page;
import com.horn.energy_blockchain.Entity.User;
import com.horn.energy_blockchain.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private IUserService IUserService;

    /**
     * 分页查询
     * @param page
     * @return
     */
    @PostMapping("/api/user/list")
    public Result userList(@RequestBody Page page){
        try{
            return new Result(200,"", IUserService.selectUserPage(page));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new Result(400, e.getMessage(), "");
        }
    }

    /**
     * 添加
     * @param user
     * @return
     */
    @PostMapping("/api/user/add")
    public Result addUser(@RequestBody User user){
        try {
            return new Result(200,"", IUserService.addUser(user));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Result(400, e.getMessage(), "");
        }
    }

    /**
     * 更新
     * @param user
     * @return
     */
    @PostMapping("/api/user/update")
    public Result updateUser(@RequestBody User user){
        try {
            return new Result(200,"", IUserService.updateUser(user));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, e.getMessage(), "");
        }
    }

    /**
     * 删除
     * @param user
     * @return
     */
    @PostMapping("/api/user/delete")
    public Result deleteUser(@RequestBody User user){
        try {
            Integer id = user.getId();
            return new Result(200,"", IUserService.deleteUser(id));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, e.getMessage(), "");
        }
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/api/user/deleteBatch")
    public Result batchDeleteUser(@RequestBody List<Integer> ids){
        try {
            System.out.println("delete/batch");
            for(int id:ids) System.out.println(id);
            IUserService.batchDelete(ids);
            return new Result(200,"","");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Result(400, e.getMessage(), "");
        }
    }
}
