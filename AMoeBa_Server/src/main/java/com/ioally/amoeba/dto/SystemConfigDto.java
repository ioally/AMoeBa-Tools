package com.ioally.amoeba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 系统配置信息
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemConfigDto {

    /**
     * 是否发送电子邮件
     */
    private Boolean sendEmail;

    /**
     * 是否允许生成密钥
     */
    private Boolean switchFlagKey;

    /**
     * 登录是否需要校验key
     */
    private Boolean isVerifyKey;

    /**
     * 是否多线程提交任务
     */
    private boolean multiThread;

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getSwitchFlagKey() {
        return switchFlagKey;
    }

    public void setSwitchFlagKey(Boolean switchFlagKey) {
        this.switchFlagKey = switchFlagKey;
    }

    public Boolean getVerifyKey() {
        return isVerifyKey;
    }

    public void setVerifyKey(Boolean verifyKey) {
        isVerifyKey = verifyKey;
    }

    public boolean isMultiThread() {
        return multiThread;
    }

    public void setMultiThread(boolean multiThread) {
        this.multiThread = multiThread;
    }
}
