package com.ioally.amoeba.api;


import com.ioally.amoeba.dto.BatchExecuteDto;
import com.ioally.amoeba.dto.FeedBackDto;
import com.ioally.amoeba.dto.LoginDto;
import com.ioally.amoeba.dto.ResponseDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 阿米巴批量填写工具Api
 *
 * @date: 2018/3/29 17:11
 * @author: 何伟东
 */
public interface AMoeBaApi {

    String PATH = "amoeba";

    /**
     * 登录
     *
     * @param loginDto 登录的用户信息
     * @return 登陆成功-true
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    ResponseDto login(LoginDto loginDto) throws Exception;

    /**
     * 查询是否已经登录
     *
     * @return 登陆的信息
     */
    @RequestMapping(value = "/queryLogin", method = {RequestMethod.POST})
    ResponseDto queryLogin() throws Exception;

    /**
     * 注销登陆
     *
     * @return 是否操作成功
     * @throws Exception
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    ResponseDto logout() throws Exception;

    /**
     * 批量保存
     *
     * @param batchExecuteDto 保存数据
     * @return
     */
    @RequestMapping(value = "/batchSave", method = {RequestMethod.POST})
    ResponseDto batchSave(BatchExecuteDto batchExecuteDto) throws Exception;

    /**
     * 按照日志id批量删除
     *
     * @param logIds 日志id集合
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = {RequestMethod.POST})
    ResponseDto batchDelete(List<String> logIds) throws Exception;

    /**
     * 查询指定时间的日志
     *
     * @param param startDate-开始时间;endDate-结束时间
     * @return
     */
    @RequestMapping(value = {"/getLog"}, method = {RequestMethod.POST})
    ResponseDto getLog(Map<String, String> param) throws Exception;

    /**
     * 查询当年的日志
     *
     * @return
     */
    @RequestMapping(value = {"/getAllLog"}, method = {RequestMethod.POST, RequestMethod.GET})
    ResponseDto getAllLog() throws Exception;

    /**
     * 新增反馈信息
     *
     * @param feedBackDto 反馈信息
     * @return
     */
    @RequestMapping(value = {"/addFeedBack"}, method = {RequestMethod.POST, RequestMethod.GET})
    ResponseDto addFeedBack(FeedBackDto feedBackDto) throws Exception;

    /**
     * 校验用户是否有指定路径的访问权限
     *
     * @param param userName - 用户名，targetUrl - 目标地址
     * @return
     */
    @RequestMapping(value = {"/checkAccess"}, method = {RequestMethod.POST, RequestMethod.GET})
    ResponseDto checkAccess(Map<String, String> param) throws Exception;

    /**
     * 用于执行sql的服务，返回查询结果或者更新行数
     *
     * @param param sql - sql脚本
     * @return
     */
    @RequestMapping(value = {"/sqlExecute"}, method = {RequestMethod.POST, RequestMethod.GET})
    ResponseDto sqlExecute(Map<String, String> param) throws Exception;
}
