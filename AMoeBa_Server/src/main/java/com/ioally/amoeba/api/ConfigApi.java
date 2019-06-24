package com.ioally.amoeba.api;

import com.ioally.amoeba.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface ConfigApi {

    String PATH = AMoeBaApi.PATH + "/config";

    /**
     * 获取系统配置信息
     *
     * @return
     */
    @RequestMapping(value = "sys", method = {RequestMethod.GET, RequestMethod.POST})
    ResponseDto sys();

}
