package com.ioally.amoeba.service;

public interface SecurityService {

    /**
     * 密钥时间戳策略cutoff-截止日期
     */
    String DATE_STRATEGY_CUTOFF = "cutoff";

    /**
     * 密钥时间戳策略begin-开始日期
     */
    String DATE_STRATEGY_BEGIN = "begin";

    /**
     * 生成新的密钥(默认有效期)
     *
     * @param userName         用户id
     * @param toEmail          发送的邮箱
     * @param isIgnoreExistKey 是否忽略已经存在的密钥
     * @return 密钥串
     */
    String generateKey(String userName, String toEmail, boolean isIgnoreExistKey) throws Exception;

    /**
     * 验证密钥是否可用
     *
     * @param userName 用户id
     * @param key      密钥
     * @return 验证结果
     */
    String verifyKey(String userName, String key) throws Exception;

    /**
     * 根据用户名获取用户的密钥
     *
     * @param userName 用户的userName
     * @return 密钥串
     */
    String getKeyByUserName(String userName) throws Exception;
}
