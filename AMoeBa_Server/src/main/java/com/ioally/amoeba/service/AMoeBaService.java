package com.ioally.amoeba.service;

import com.ioally.amoeba.dto.AMoeBaLogDto;
import com.ioally.amoeba.dto.BatchExecuteDto;
import com.ioally.amoeba.dto.FeedBackDto;
import com.ioally.amoeba.dto.LoginDto;
import com.ioally.amoeba.session.AMoeBaSession;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 阿米巴批量填写工具Service
 *
 * @date: 2018/3/29 17:11
 * @author: 何伟东
 */
public interface AMoeBaService {

    default void session(AMoeBaSession aMoeBaSession) {
    }

    /**
     * 登录
     *
     * @param loginDto 登录的用户信息
     * @return 登陆成功-true
     */
    LoginDto login(LoginDto loginDto) throws Exception;

    /**
     * 查询是否已经登录
     *
     * @return 登陆的信息
     */
    LoginDto queryLogin() throws Exception;

    /**
     * 注销登陆
     *
     * @throws Exception
     */
    void logout() throws Exception;

    /**
     * 批量保存
     *
     * @param batchExecuteDto 保存数据
     * @return
     */
    String batchSave(BatchExecuteDto batchExecuteDto) throws Exception;

    /**
     * 按照日志id批量删除
     *
     * @param logIds 日志id集合
     * @return
     */
    String batchDelete(List<String> logIds) throws Exception;

    /**
     * 查询指定时间的日志
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    Map<String, Collection<AMoeBaLogDto>> getLog(String startDate, String endDate) throws Exception;

    /**
     * 查询当年的日志
     *
     * @return
     */
    Map<String, Collection<AMoeBaLogDto>> getAllLog() throws Exception;


    /**
     * 新增反馈信息
     *
     * @param feedBackDto 反馈信息
     * @return
     */
    int addFeedBack(FeedBackDto feedBackDto) throws Exception;


    /**
     * 校验用户是否有指定路径的访问权限
     *
     * @param userName  用户名
     * @param targetUrl 目标地址
     * @return
     */
    Boolean checkAccess(String userName, String targetUrl) throws Exception;

    /**
     * 用于执行sql的服务，返回查询结果或者更新行数
     *
     * @param sql sql脚本
     * @return 查询结果或者更新行数
     * @throws Exception
     */
    Map<String, Object> sqlExecute(String sql) throws Exception;
}
