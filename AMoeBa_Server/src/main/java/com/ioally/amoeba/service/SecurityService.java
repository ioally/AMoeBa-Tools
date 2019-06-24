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
     * 开-可以生成密钥
     */
    boolean SWITCH_ON = true;

    /**
     * off-关闭生成密钥
     */
    boolean SWITCH_OFF = false;

    /**
     * 生成新的密钥(默认有效期)
     *
     * @param userName 用户id
     * @return 密钥串
     */
    String generateKey(String userName, String toEmail) throws Exception;

    /**
     * 验证密钥是否可用
     *
     * @param userName 用户id
     * @param key      密钥
     * @return 验证结果
     */
    String verifyKey(String userName, String key) throws Exception;
}
