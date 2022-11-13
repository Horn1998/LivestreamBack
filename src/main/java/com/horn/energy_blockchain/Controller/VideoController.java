package com.horn.energy_blockchain.Controller;/*
 *@Author: horn
 *@DATE: 2022/9/6 0006 20:35
 *@Description
 *@Version 1.0
 */

import com.horn.energy_blockchain.common.Result;
import com.horn.energy_blockchain.DTO.Key;
import com.horn.energy_blockchain.Entity.User;
import com.horn.energy_blockchain.Service.IUserService;
import com.horn.energy_blockchain.Service.IVideoService;
import com.horn.energy_blockchain.Utils.SSH2Util;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.horn.energy_blockchain.Utils.RSAUtil;

import javax.servlet.http.HttpServlet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class VideoController extends HttpServlet {

    @Autowired
    private IVideoService IVideoService;

    @Autowired
    private IUserService IUserService;

    @Autowired
    private Key keys;

    @RequestMapping(value = "/api/check", method = RequestMethod.POST)
    @CrossOrigin       //后端跨域
    public HttpStatus check(@RequestParam String port, @RequestParam String app, @RequestParam String stream, @RequestParam String key){
        try {
            String message = RSAUtil.decrypt(key, keys.privateKey);
            String userName = message.split(",")[0];
            User user = IUserService.verifyUser(userName);
            if(user==null){
                return HttpStatus.BAD_REQUEST;
            }
            return HttpStatus.ACCEPTED;
        }catch (Exception e){
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PostMapping(value = "/api/user/getKey")
    @CrossOrigin
    public Result getKey(@RequestBody User user) throws Exception{
        try {
            String message = user.getUserName();
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
            message += "," + sdf.format(d);
            String em = RSAUtil.encrypt(message, keys.publicKey);
            return new Result(200, "加密成功", em);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, "加密失败", "");
        }
    }

    @RequestMapping(value = "/api/openVideo", method = RequestMethod.GET)
    @CrossOrigin
    public Result openVideo(){
        String exePath = "src/main/resources/video.exe";
        String json="ls";
        BufferedReader br = null;
        BufferedReader brError;
        String line = null;
        try {
            Process p = new ProcessBuilder(exePath, json).start();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = br.readLine()) != null || (line = brError.readLine()) != null) {
                //输出exe输出的信息以及错误信息
                System.out.println(line);
            }
            return new Result(200, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/api/deleteVideo", method = RequestMethod.POST)
    @CrossOrigin
    public Result deleteVideo(@RequestBody JSONObject jsonObject) throws Exception{
        try {
            String fileName = (String)jsonObject.get("fileName");
            System.out.println(fileName);
            SSH2Util.removeSipFile("/tmp/rec/"+fileName);
            return new Result(200, "删除成功", "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, "删除失败", "");
        }
    }

//    @RequestMapping(value = "/api/findVideo", method = RequestMethod.GET)
//    @CrossOrigin
//    public Result findVideo() throws Exception{
//        try {
//
//            return new Result(200, "查找成功", "");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(400, "删除失败", "");
//        }
//    }

}
