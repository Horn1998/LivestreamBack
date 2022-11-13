package com.horn.energy_blockchain.Controller;

import com.horn.energy_blockchain.Entity.Chatroom;
import com.horn.energy_blockchain.Service.impl.MessageServiceImpl;
import com.horn.energy_blockchain.Service.impl.RoomServiceImpl;
import com.horn.energy_blockchain.Service.impl.SignServiceImpl;
import com.horn.energy_blockchain.common.Result;
import com.horn.energy_blockchain.Entity.Message;
import com.horn.energy_blockchain.Service.IMessageService;
import com.horn.energy_blockchain.Utils.RabbitmqServer;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DELL
 * @date 2020/5/20 18:43
 */
@RestController
@CrossOrigin
@RequestMapping("/api/message")
public class ChatController {

    @Autowired
    MessageServiceImpl messageService;

    @Autowired
    RabbitmqServer rabbitmqServer;

    @Autowired
    RoomServiceImpl roomService;

    @Autowired
    SignServiceImpl signService;
    /**
     * 测试
     * @return
     */
    @GetMapping("test")
    public String test(){
        return "YES!";
    }

    /**
     * 添加用户
     */
    @PostMapping("save")
    public Map<String,Object> save(@RequestBody Message message){
        HashMap<String, Object> map = new HashMap<>();
        try {
            messageService.saveMessage(message);
            map.put("success",true);
            map.put("msg","添加弹幕成功");
        }catch (Exception e){
            e.printStackTrace();
            map.put("success",false);
            map.put("msg","添加弹幕失败: " + e.getMessage());
        }
        return map;
    }

    /**
     * 为房间生成队列
     * */
    @RequestMapping(value="/create", method = RequestMethod.POST)
    public Result create(@RequestBody JSONObject jsonObject) throws Exception {
        try {

            //为当前直播间创建一个队列存储数据
            String room = jsonObject.getStr("room");
            String name = jsonObject.getStr("name");
            //第一次创建房间，保存进入数据库
            room = roomService.judgeAndInsert(room, name);
            rabbitmqServer.run(room);
            return new Result(200, "", room);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, "","");
        }
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody JSONObject jsonObject){
        try {
            String room = jsonObject.getStr("room");
            rabbitmqServer.run(room);
            //根据room查找弹幕
            List<Message> results = messageService.findByRoom(room);
            return new Result(200, "", results);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, "error", "");
        }
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result findAll(){
        try {
            List<Chatroom> results = roomService.getRooms();
            return new Result(200, "", results);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, "", "");
        }
    }

    @RequestMapping(value = "/deleteRoom", method = RequestMethod.POST)
    public Result deleteRoom(@RequestBody JSONObject jsonObject){
        try {
            String room = jsonObject.getStr("room");
            String name = jsonObject.getStr("name");
            //判断用户有没有删除room的权限
            if(roomService.removeRoom(room, name)){
                messageService.removeByRoom(room);
                return new Result(200, "yes", "");
            }
            return new Result(200, "no", "");
        } catch (Exception e) {
            return new Result(400, "error", "");
        }
    }

    //用户签到
    @RequestMapping(value = "/doSign", method = RequestMethod.POST)
    public Result doSign(@RequestBody JSONObject jsonObject){
        try {
            String name = jsonObject.getStr("user");
            String room = jsonObject.getStr("room");
            LocalDate date = LocalDate.now(); // get the current date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String now = date.format(formatter);
            if(!signService.checkRoom(room)) return new Result(200, "没有签到任务", "");
            //判断用户有没有删除room的权限
            if(!signService.checkSign(room, name, now)){
                return new Result(200, "签到成功", "");
            }
            return new Result(200, "已经签到", "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(400, "已经签到", "");
        }
    }

    //签到发起
    @RequestMapping(value = "/pubSign", method = RequestMethod.POST)
    public Result pubSign(@RequestBody JSONObject jsonObject){
        try {
            LocalDate date = LocalDate.now(); // get the current date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String now = date.format(formatter);
            String name = jsonObject.getStr("user");
            String room = jsonObject.getStr("room");
            signService.signForAll(room, name, now);
            return new Result(200, "", "");
        } catch (Exception e) {
            return new Result(400, "已经签到", "");
        }
    }

    //获取角色
    @RequestMapping(value = "/getRole", method = RequestMethod.POST)
    public Result getRole(@RequestBody JSONObject jsonObject){
        try {
            String name = jsonObject.getStr("name");
            String room = jsonObject.getStr("room");
            if(roomService.countByNameAndRoom(room, name) == 1){
                if(signService.checkRoom(room)) return new Result(200, "2", ""); //签到已经发起
                return new Result(200, "1", ""); //签到未发起
            }
            return new Result(200, "0", ""); //普通签到用户
        } catch (Exception e) {
            return new Result(400, "没有该角色", "");
        }
    }

    //停止签到
    @RequestMapping(value = "/stopSign", method = RequestMethod.POST)
    public Result stopSign(@RequestBody JSONObject jsonObject){
        try {
            String room = jsonObject.getStr("room");
            signService.stopSign(room);
            return new Result(200, "", "");
        } catch (Exception e) {
            return new Result(400, "", "");
        }
    }

}
