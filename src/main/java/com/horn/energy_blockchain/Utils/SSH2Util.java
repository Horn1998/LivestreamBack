package com.horn.energy_blockchain.Utils;/*
 *@Author: horn
 *@DATE: 2022/9/15 0015 16:45
 *@Description
 *@Version 1.0
 */

import ch.ethz.ssh2.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SSH2Util {
    //服务器ip地址
    private final static String host="10.128.206.152";
//    private final static String host="10.112.180.180";
    //账户
//    private final static String userName="dell";
    private final static String userName="dell";
    //密码
    private final static String passWord="ml509509";
//    private final static String passWord="ml509509";


    //指定默认编码
    private static String DEFAULT_CHARSET = "UTF-8";

    /**
     * 建立SSH2连接
     * @param host 主机地址
     * @param username 用户名
     * @param password 密码
     * @return Connection
     */
    public static Connection openConnection(String host, String username, String password) {
        try {
            Connection connection = new Connection(host);
            //建立ssh2连接
            connection.connect();
            //检验用户名
            boolean login = connection.authenticateWithPassword(username,password);
            if (login){
                return connection;
            }else {
                throw new RuntimeException(host + " 用户名密码不正确");
            }
        } catch (Exception e) {
            throw new RuntimeException(host +" "+ e);
        }
    }

    /**
     * 读取服务器文件
     * @param fileName 路径+文件名+文件后缀
     * @return
     */
    public static String readSipFile(String filePath,String fileName){

        Connection conn = SSH2Util.openConnection(host,userName,passWord);;
        String url = null;
        try {

            //获取文件流
            SCPClient scpClient = conn.createSCPClient();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream bufferedInputStream = scpClient.get(filePath);

            //这里做其他操作   下载本地 上传OSS存储服务之类的
            //文件上传阿里OSS存储
            //url = OSSUploadUtils.upload(bufferedInputStream,fileName);

        }catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            log.info("读取文件失败：" + fileName);
        }finally {

            if (null != conn) {
                conn.close();
            }
        }
        return url;
    }

    /**
     * 删除服务器文件
     * @param fileName 路径+文件名+文件后缀
     * @return
     */
    public static void removeSipFile(String fileName){

        Connection conn = SSH2Util.openConnection(host,userName,passWord);

        Session session = null;
        try {
            //执行命令
            session = conn.openSession();

            //执行删除命令
            session.execCommand("rm -f "+fileName);

        }catch (Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
            log.info("文件删除失败：" + fileName);
        }finally {
            if (null != session){
                session.close();
            }
            if (null != conn) {
                conn.close();
            }
        }
    }

    //读取服务器文件名
    public static ArrayList<String> readFile(String directory) {
        //创建远程连接，默认连接端口为22，如果不使用默认，可以使用方法
        //new Connection(ip, port)创建对象
        String ip = host;
        String usr = userName;
        String pwd = passWord;
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
