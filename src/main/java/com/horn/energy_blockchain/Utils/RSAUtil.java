package com.horn.energy_blockchain.Utils;/*

 *@Author: horn
 *@DATE: 2022/9/6 0006 20:29
 *@Description
 *@Version 1.0
 */

import com.xiaoleilu.hutool.crypto.asymmetric.KeyType;
import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.HexUtil;
import com.xiaoleilu.hutool.util.StrUtil;

public class RSAUtil {
    //加密
    public static String encrypt(String words, String PublicKey) {
        RSA rsa = new RSA(null, PublicKey);

        String encryptWords = rsa.encryptStr(words, KeyType.PublicKey);

        return encryptWords;
    }

    //解密
    public static String decrypt(String encryptWords, String PrivateKey) {
        try {
            RSA rsa = new RSA(PrivateKey, null);
            byte[] encryptWordsByte = HexUtil.decodeHex(encryptWords);
            byte[] decryptWordsByte = rsa.decrypt(encryptWordsByte, KeyType.PrivateKey);
            String decryptWords = StrUtil.str(decryptWordsByte, CharsetUtil.CHARSET_UTF_8);
            return decryptWords;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
