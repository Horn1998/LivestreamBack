package com.horn.energy_blockchain.Utils;/*
 *@Author: horn
 *@DATE: 2022/9/11 0011 22:51
 *@Description
 *@Version 1.0
 */

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3DirectoryEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class QueryLinuxFileNames {
    public static ArrayList<String> readFile(String directory) {
        //创建远程连接，默认连接端口为22，如果不使用默认，可以使用方法
        //new Connection(ip, port)创建对象
        String ip = "192.168.43.146";
        String usr = "horn1998";
        String pwd = "wzh199810";
        //int port=22;
        Connection conn = null;
        String date = "";
        //所要查询的目录
        String path = directory;
        ArrayList<String> filesNameList = new ArrayList<String>();        try {
            //连接远程服务器
            // 连接部署服务器
            conn = new Connection(ip);
            conn.connect();
            //使用用户名和密码登录
            boolean b = conn.authenticateWithPassword(usr, pwd);
            if (!b) {
                throw new IOException("Authentication failed.");
            } else {
                SFTPv3Client sft = new SFTPv3Client(conn);
                List<?> v = sft.ls(path);
                for (int i = 0; i < v.size(); i++) {
                    SFTPv3DirectoryEntry s = new SFTPv3DirectoryEntry();
                    s = (SFTPv3DirectoryEntry) v.get(i);
                    //文件名
                    String filename = s.filename;
                    filesNameList.add(filename);
                }
            }
            conn.close();
        } catch (IOException e) {
            System.err.printf("用户%s密码%s登录服务器%s失败！", usr, pwd, ip);
            e.printStackTrace();
        }

        return filesNameList;
    }

}
