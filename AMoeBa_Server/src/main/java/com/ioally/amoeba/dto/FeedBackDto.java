package com.ioally.amoeba.dto;

import com.ioally.amoeba.utils.other.DateUtil;

import java.util.Date;
import java.util.UUID;

public class FeedBackDto extends BaseRequestDto {

    /**
     * 反馈信息id
     */
    private String id = UUID.randomUUID().toString().toUpperCase();
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 反馈创建人
     */
    private String createBy;
    /**
     * 反馈时间
     */
    private String createTime = DateUtil.sdf_en.format(new Date());
    /**
     * 反馈人邮箱
     */
    private String email;
    /**
     * 是否已经发送邮件标志 0-未发送
     */
    private String isSendFlag = "0";

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsSendFlag() {
        return isSendFlag;
    }

    public void setIsSendFlag(String isSendFlag) {
        this.isSendFlag = isSendFlag;
    }
}
