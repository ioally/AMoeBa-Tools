package com.ioally.amoeba.service.impl;

import com.ioally.amoeba.service.MailService;
import com.ioally.amoeba.service.SecurityService;
import com.ioally.amoeba.tasks.SimpleEmailThread;
import com.ioally.amoeba.utils.other.DateUtil;
import com.ioally.amoeba.utils.security.SecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Value("${amoeba.security.validTime}")
    private int validTime;

    @Value("${amoeba.security.aesKey}")
    private String aesKey;

    @Value("${amoeba.security.dateStrategy}")
    private String dateStrategy;

    @Value("${amoeba.security.switchFlag}")
    private boolean switchFlag;

    /**
     * 生成密钥失败后的重试次数
     */
    private static final int reTry = 5;

    @Value("${amoeba.security.sendEmail}")
    private boolean sendEmail;

    @Autowired
    private MailService mailService;

    /**
     * 生成新的密钥(默认有效期)
     *
     * @param userName 用户id
     * @return 密钥串
     */
    private String generateKey(String userName) throws Exception {
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("用户id不能为空！");
        }
        if (switchFlag) {
            Date nowDate = new Date();
            switch (dateStrategy) {
                case DATE_STRATEGY_BEGIN:
                    break;
                case DATE_STRATEGY_CUTOFF:
                    nowDate = DateUtil.calculateDate(DateUtil.DAY_OF_YEAR, nowDate, validTime);
                    break;
                default:
                    throw new RuntimeException("密钥时间戳策略配置有误无法生成密钥！");
            }
            String encodeByAESToBase64 = null;
            try {
                encodeByAESToBase64 = SecurityUtil.encodeByAESToBase64(userName, nowDate, aesKey);
            } catch (Exception e) {
                LOGGER.error("密钥生成失败！", e);
            }
            for (int i = 0; i < reTry; i++) {
                try {
                    this.verifyKey(userName, encodeByAESToBase64);
                    break;
                } catch (Exception e) {
                    LOGGER.error("[{}]密钥生成成功，但验证未通过，重新生成！", encodeByAESToBase64);
                    encodeByAESToBase64 = generateKey(userName);
                }
            }
            if (StringUtils.isNotEmpty(encodeByAESToBase64)) {
                LOGGER.info("用户{},成功生成密钥{}", userName, encodeByAESToBase64);
            }
            return encodeByAESToBase64;
        } else {
            throw new RuntimeException("系统已关闭密钥生成功能，暂无法生成密钥！");
        }
    }

    /**
     * 生成一个密钥并且发送邮件
     *
     * @param userName 用户名
     * @param toEmail  邮箱地址
     * @throws Exception
     */
    @Override
    public String generateKey(String userName, String toEmail) throws Exception {
        String key = this.generateKey(userName);
        if (StringUtils.isNotEmpty(toEmail) && sendEmail) {
            SimpleEmailThread
                    .instance(mailService, userName, key, verifyKey(userName, key))
                    .to(toEmail)
                    .send();
        }
        return key;
    }

    /**
     * 验证密钥是否可用
     *
     * @param userName 用户id
     * @param key      密钥
     * @return 验证结果
     */
    @Override
    public String verifyKey(String userName, String key) throws Exception {
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("用户id不可为空！");
        }
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("密钥不能为空！");
        }
        Date date;
        try {
            date = SecurityUtil.decodeBase64ByAES(userName, key, aesKey);
        } catch (Exception e) {
            throw new Exception("密钥无效！");
        }
        Date nowDate = new Date();
        boolean isTimeOut;
        switch (dateStrategy) {
            case DATE_STRATEGY_BEGIN:
                isTimeOut = DateUtil.isTimeout(date, validTime * 24 * 60);
                break;
            case DATE_STRATEGY_CUTOFF:
                isTimeOut = nowDate.compareTo(date) > 0;
                break;
            default:
                throw new RuntimeException("密钥时间戳策略配置有误，无法验证密钥！");
        }
        String resultDateStr = DATE_STRATEGY_BEGIN.equalsIgnoreCase(dateStrategy) ?
                DateUtil.sdf_zh.format(DateUtil.calculateDate(DateUtil.DAY_OF_YEAR, date, validTime)) :
                DateUtil.sdf_zh.format(date);
        if (isTimeOut) {
            throw new Exception("密钥已于[" + resultDateStr + "]过期！");
        }
        return resultDateStr;
    }
}
