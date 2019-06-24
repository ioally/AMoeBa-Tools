package com.ioally.amoeba.controller;

import com.ioally.amoeba.api.SecurityApi;
import com.ioally.amoeba.dto.ResponseDto;
import com.ioally.amoeba.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = SecurityApi.PATH)
public class SecurityController implements SecurityApi {

    @Autowired
    private SecurityService securityService;

    /**
     * 生成新的密钥
     *
     * @param param 生成密钥需要的数据
     * @return 密钥串
     */
    @Override
    public ResponseDto generateKey(@RequestBody Map<String, String> param) throws Exception {
        return ResponseDto.newInstance(securityService.generateKey(param.get("userName"), param.get("email")));
    }

    /**
     * 验证密钥是否可用
     *
     * @param param 验证密钥需要的参数
     * @return 验证结果
     */
    @Override
    public ResponseDto verifyKey(@RequestBody Map<String, String> param) throws Exception {
        return ResponseDto.newInstance(securityService.verifyKey(param.get("userName"), param.get("key")));
    }
}
