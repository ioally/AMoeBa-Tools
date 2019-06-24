package com.ioally.amoeba.service.impl;

import com.ioally.amoeba.dto.MailInfoDto;
import com.ioally.amoeba.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {


    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromUser;


    /**
     * 发送一封简单email，没有抄送人和密送人
     *
     * @param mailInfoDto
     * @return
     */
    @Override
    public String sendSimpleEmail(MailInfoDto mailInfoDto) {
        String from;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        String fromAlias = mailInfoDto.getFromAlias();
        if (StringUtils.isNotEmpty(fromAlias)) {
            from = fromAlias + "<" + fromUser + ">";
        } else {
            from = fromUser;
        }
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(mailInfoDto.getToEmail());
        simpleMailMessage.setSubject(mailInfoDto.getSubject());
        simpleMailMessage.setText(mailInfoDto.getText());
        javaMailSender.send(simpleMailMessage);
        return "0";
    }
}
