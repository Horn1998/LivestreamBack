package com.horn.energy_blockchain.Controller;/*
 *@Author: horn
 *@DATE: 2022/9/11 0011 22:57
 *@Description
 *@Version 1.0
 */
import com.horn.energy_blockchain.common.Result;
import com.horn.energy_blockchain.Utils.SSH2Util;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ServerController {

   @RequestMapping(value="/api/filelist", method = RequestMethod.GET)
   @CrossOrigin
   public Result fileList(){
       try{
           List<String> files = SSH2Util.readFile("/tmp/rec");
           List<JSONObject> jsonFile = new ArrayList<>();
           int id = 0;
           for(String file:files){
               if(file.equals(".") || file.equals("..")) continue;
               JSONObject json = new JSONObject();
               json.put("name", "视频"+Integer.toString(id));
               id += 1;
               json.put("id", Integer.toString(id));
               json.put("date", file);
               jsonFile.add(json);
           }
           return new Result(200, "", jsonFile);
       }catch (Exception e){
           e.printStackTrace();
           System.out.println(400);
           return new Result(400, e.getMessage(), "");
       }
   }



}
