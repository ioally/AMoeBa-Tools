package com.ioally.amoeba.tasks;

import com.ioally.amoeba.dto.MailInfoDto;
import com.ioally.amoeba.service.MailService;

/**
 * 新启动一个线程发送邮件
 */
public class SimpleEmailThread extends EmailThread {

    /**
     * 发送邮件的服务
     */
    private MailService mailService;

    private String userName;

    private String key;

    private String verifyDate;

    private String toEmail;

    private SimpleEmailThread(MailService mailService, String userName, String key, String verifyDate) {
        this.mailService = mailService;
        this.userName = userName;
        this.key = key;
        this.verifyDate = verifyDate;
    }

    public static SimpleEmailThread instance(MailService mailService, String userName, String key, String verifyDate) {
        return new SimpleEmailThread(mailService, userName, key, verifyDate);
    }

    @Override
    public void run() {
        mailService.sendSimpleEmail(new MailInfoDto.Builder()
                .fromAlias("AMoeBa Tools")
                .subject("【AMoeBa Tools】您的密钥已生成")
                .toEmail(toEmail)
                .text("尊敬的用户您好！\n使用AMoeBa Tools需要验证您的身份，您绑定的登录工号为：【"
                        + userName + "】，系统已经为您生成密钥（请妥善保管）：\n\n"
                        + key
                        + "\n\n密钥有效期至："
                        + verifyDate
                        + "，请使用【" + userName + "】登录！")
                .build());
    }

    @Override
    public SimpleEmailThread to(String toEmail) {
        this.toEmail = toEmail;
        return this;
    }

    public void send() {
        start();
    }
}