package com.ioally.amoeba.dto;

/**
 * 密钥信息
 */
public class KeyInfoDto {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户对应的密钥
     */
    private String key;
    /**
     * 用户密钥加密的私钥
     */
    private String privateKey;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
