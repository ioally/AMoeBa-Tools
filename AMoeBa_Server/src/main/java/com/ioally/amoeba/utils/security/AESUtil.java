package com.ioally.amoeba.utils.security;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    // 密钥长度
    private static final int KEY_SIZE = 256;
    // 加密类型
    private static final String ENCRYPTION_MODE_AES = "AES";

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 密钥指纹字符串
     * @return 密文
     */
    public static byte[] encrypt(String content, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE_AES);// 创建密码器
        byte[] byteContent = content.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, initKeyForAES(KEY_SIZE, password));// 初始化为加密模式的密码器
        return cipher.doFinal(byteContent);// 加密
    }

    /**
     * AES加密字符串
     *
     * @param content  要加密的内容
     * @param password 密钥指纹字符串
     * @return 加密后的Base64字符串
     * @throws Exception
     */
    public static String encryptToBase64(String content, String password) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(content, password));
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 密钥指纹字符串
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String password) throws Exception {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_MODE_AES);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, initKeyForAES(KEY_SIZE, password));// 初始化为解密模式的密码器
        return cipher.doFinal(content);
    }

    /**
     * 解密AES加密过的Base64字符串
     *
     * @param contentBase64 加密后的base64字符串
     * @param password      密钥指纹字符串
     * @return 解密的结果
     * @throws Exception
     */
    public static byte[] decryptByBase64(String contentBase64, String password) throws Exception {
        return decrypt(Base64.getDecoder().decode(contentBase64), password);
    }

    /**
     * 获取密钥
     *
     * @param key 密钥指纹
     * @return 密钥
     * @throws NoSuchAlgorithmException
     */
    public static Key initKeyForAES(int length, String key) throws NoSuchAlgorithmException {
        if (StringUtils.isEmpty(key)) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec secretKeySpec;
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(key.getBytes());
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_MODE_AES);
        keyGenerator.init(length, random);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        secretKeySpec = new SecretKeySpec(enCodeFormat, ENCRYPTION_MODE_AES);
        return secretKeySpec;
    }
}
