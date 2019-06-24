package com.ioally.amoeba.service;

import com.ioally.amoeba.dto.MailInfoDto;


public interface MailService {

    String sendSimpleEmail(MailInfoDto mailInfoDto);
}
