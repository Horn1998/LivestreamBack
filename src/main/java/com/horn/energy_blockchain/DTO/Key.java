package com.horn.energy_blockchain.DTO;/*
 *@Author: horn
 *@DATE: 2022/9/6 0006 21:26
 *@Description
 *@Version 1.0
 */

import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class Key {
    public String privateKey;
    public String publicKey;

    Key() throws IOException {
        try {
            File pubFile = new File("pub.txt");
            File priFile = new File("pri.txt");
            if(pubFile.exists()) {
                FileInputStream in = new FileInputStream(pubFile);
                int size = in.available();
                byte[] buffer = new byte[size];
                in.read(buffer);
                in.close();
                publicKey = new String(buffer, "utf-8");
                FileInputStream out = new FileInputStream(priFile);
                size = out.available();
                //读入末尾会多一个\r\t，所以要-2
                buffer = new byte[size-2];
                out.read(buffer);
                out.close();
                privateKey = new String(buffer, "utf-8");
            }else{
                RSA rsa = new RSA();
                privateKey = rsa.getPrivateKeyBase64();
                publicKey = rsa.getPublicKeyBase64();
                System.out.println(privateKey.length());
                PrintStream ps = new PrintStream(new FileOutputStream(pubFile));
                ps.println(publicKey);// 往文件里写入字符串
                ps = new PrintStream(new FileOutputStream(priFile));
                ps.println(privateKey);// 往文件里写入字符串
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
