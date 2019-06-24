package com.ioally.amoeba.controller;

import com.ioally.amoeba.api.AMoeBaApi;
import com.ioally.amoeba.dto.BatchExecuteDto;
import com.ioally.amoeba.dto.FeedBackDto;
import com.ioally.amoeba.dto.LoginDto;
import com.ioally.amoeba.dto.ResponseDto;
import com.ioally.amoeba.exception.UserPermissionException;
import com.ioally.amoeba.service.AMoeBaService;
import com.ioally.amoeba.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 阿米巴批量填写工具Controller
 *
 * @date: 2018/3/29 17:11
 * @author: 何伟东
 */
@RestController
@RequestMapping(value = AMoeBaApi.PATH)
public class AMoeBaController implements AMoeBaApi {

    @Autowired
    private AMoeBaService aMoeBaService;

    @Autowired
    private SecurityService securityService;

    @Value("${amoeba.isVerifyKey}")
    private boolean isVerifyKey;

    /**
     * 登录
     *
     * @param loginDto 登录的用户信息
     * @return 登陆成功-true
     */
    @Override
    public ResponseDto login(@RequestBody LoginDto loginDto) throws Exception {
        if (loginDto == null) {
            throw new UserPermissionException("登陆信息不能为空！");
        }
        // 系统若开启验证密钥，则先验证密钥信息
        if (isVerifyKey) {
            securityService.verifyKey(loginDto.getUserName(), loginDto.getKey());
        }
        return ResponseDto.newInstance(aMoeBaService.login(loginDto.getUserName(), loginDto.getPassWord()));
    }

    /**
     * 查询是否已经登录
     *
     * @return 登陆的信息
     */
    @Override
    public ResponseDto queryLogin() throws Exception {
        return ResponseDto.newInstance(aMoeBaService.queryLogin());
    }

    /**
     * 注销登陆
     *
     * @return 是否操作成功
     * @throws Exception
     */
    @Override
    public ResponseDto logout() throws Exception {
        aMoeBaService.logout();
        return ResponseDto.newInstance();
    }

    /**
     * 批量保存服务
     *
     * @param batchExecuteDto 保存数据
     * @return
     */
    @Override
    public ResponseDto batchSave(@RequestBody BatchExecuteDto batchExecuteDto) throws Exception {
        return ResponseDto.newInstance(aMoeBaService.batchSave(batchExecuteDto));
    }

    /**
     * 按照日志id批量删除
     *
     * @param logIds 日志id集合
     * @return
     */
    @Override
    public ResponseDto batchDelete(@RequestBody List<String> logIds) throws Exception {
        return ResponseDto.newInstance(aMoeBaService.batchDelete(logIds));
    }

    /**
     * 查询指定时间的日志
     *
     * @param param startDate-开始时间;endDate-结束时间
     * @return
     */
    @Override
    public ResponseDto getLog(@RequestBody Map<String, String> param) throws Exception {
        return ResponseDto.newInstance(aMoeBaService.getLog(param.get("startDate"), param.get("endDate")));
    }

    /**
     * 查询当年的日志
     *
     * @return
     */
    @Override
    public ResponseDto getAllLog() throws Exception {
        return ResponseDto.newInstance(aMoeBaService.getAllLog());
    }

    /**
     * 新增反馈信息
     *
     * @param feedBackDto 反馈信息
     * @return
     */
    @Override
    public ResponseDto addFeedBack(@RequestBody FeedBackDto feedBackDto) throws Exception {
        return ResponseDto.newInstance(aMoeBaService.addFeedBack(feedBackDto));
    }

    /**
     * 校验用户是否有指定路径的访问权限
     *
     * @param param userName - 用户名，targetUrl - 目标地址
     * @return
     */
    @Override
    public ResponseDto checkAccess(@RequestBody Map<String, String> param) throws Exception {
        return ResponseDto.newInstance(aMoeBaService.checkAccess(param.get("userName"), param.get("targetUrl")));
    }

    /**
     * 用于执行sql的服务，返回查询结果或者更新行数
     *
     * @param param sql - sql脚本
     * @return
     */
    @Override
    public ResponseDto sqlExecute(@RequestBody Map<String, String> param) throws Exception {
        return ResponseDto.newInstance(aMoeBaService.sqlExecute(param.get("sql")));
    }
}
