package com.sense.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * SecurityTool.java
 * Created at 2019-07-12
 * Created by lx
 * Copyright C 20190712 SAIC SHANGHAI VOLKSWAGEN, All rights reserved.
 */
public class SecurityTool
{
    private final static String ALGORITHM = "AES";
    private final static String ENCODE = "UTF-8";
    private final static int KEYLENGTH = 128;//可选值128,192,256
    private final static String ENCRYPTSEED = "1qaz@WSX3edc";

    /**
     * 明文字符串以UTF格式转二进制字节流,在加密返回字符串
     */
    public static String encrypt(String plaintext, String password)
    {
        String ciphertext = null;
        try
        {
            // 创建AES的Key生产者
            KeyGenerator kgen = KeyGenerator.getInstance(SecurityTool.ALGORITHM);
            // 利用用户密码作为随机数初始化出
            kgen.init(SecurityTool.KEYLENGTH, new SecureRandom(password.getBytes()));
            // 128位的key生产者
            // 加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, SecurityTool.ALGORITHM);// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance(SecurityTool.ALGORITHM);// 创建密码器
            byte[] byteContent = plaintext.getBytes(SecurityTool.ENCODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            ciphertext = Base64.encodeBase64String(result);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return ciphertext;
    }

    public static String encrypt(String plaintext)
    {
        if(plaintext==null)
        {
            return null;
        }
        else
        {
            return SecurityTool.encrypt(plaintext, ENCRYPTSEED);
        }

    }

    public static String decrypt(String ciphertext)
    {
        if(ciphertext==null)
        {
            return null;
        }
        else
        {
            return SecurityTool.decrypt(ciphertext, ENCRYPTSEED);
        }

    }


    /**
     *
     * @param ciphertext
     *            密文
     * @param password
     *            密码
     * @return 明文
     */
    public static String decrypt(String ciphertext, String password)
    {
        String plaintext = null;
        try
        {
            byte[] content = Base64.decodeBase64(ciphertext);
            KeyGenerator kgen = KeyGenerator.getInstance(SecurityTool.ALGORITHM);// 创建AES的Key生产者
            kgen.init(SecurityTool.KEYLENGTH, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, SecurityTool.ALGORITHM);// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance(SecurityTool.ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            try
            {
                plaintext = new String(result, SecurityTool.ENCODE);
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return plaintext;
    }


    public static void main(String[] args)
    {
//        String pwd = "abcd123sss4";
//        String str = SecurityTool.encrypt("13311760221", pwd);
//        System.out.println(str);
//        System.out.println(str.length());
//        System.out.println(SecurityTool.decrypt(str, pwd));
//
//        System.out.println(new SecureRandom("abc".getBytes()));
//        System.out.println("Algorithm".toUpperCase());

        System.out.println(decrypt("SB8JiMgUhQ3x10AFGmOcow=="));
    }

}
