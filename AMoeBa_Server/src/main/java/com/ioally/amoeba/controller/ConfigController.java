package com.ioally.amoeba.controller;

import com.ioally.amoeba.api.ConfigApi;
import com.ioally.amoeba.dto.ResponseDto;
import com.ioally.amoeba.dto.SystemConfigDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ConfigApi.PATH)
public class ConfigController implements ConfigApi {

    @Value("${amoeba.isVerifyKey}")
    private boolean isVerifyKey;

    @Value("${amoeba.security.sendEmail}")
    private boolean sendEmail;

    @Value("${amoeba.security.switchFlag}")
    private boolean switchFlag;

    @Value("${amoeba.multiThread}")
    private boolean multiThread;


    @Override
    public ResponseDto sys() {
        SystemConfigDto systemConfigDto = new SystemConfigDto();
        systemConfigDto.setSendEmail(sendEmail);
        systemConfigDto.setSwitchFlagKey(switchFlag);
        systemConfigDto.setVerifyKey(isVerifyKey);
        systemConfigDto.setMultiThread(multiThread);
        return ResponseDto.newInstance(systemConfigDto);
    }
}
