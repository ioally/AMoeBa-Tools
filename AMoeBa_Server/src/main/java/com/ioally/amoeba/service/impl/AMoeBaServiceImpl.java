package com.ioally.amoeba.service.impl;

import com.ioally.amoeba.dao.SqliteDao;
import com.ioally.amoeba.dto.*;
import com.ioally.amoeba.service.AMoeBaService;
import com.ioally.amoeba.session.AMoeBaSession;
import com.ioally.amoeba.session.SessionManager;
import com.ioally.amoeba.tasks.AMoeBaExecuteTask;
import com.ioally.amoeba.utils.other.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * 阿米巴批量填写工具ServiceImpl
 *
 * @date: 2018/3/29 17:11
 * @author: 何伟东
 */
@Service
public class AMoeBaServiceImpl implements AMoeBaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AMoeBaServiceImpl.class);

    private AMoeBaSession aMoeBaSession;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private SqliteDao sqliteDao;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${amoeba.multiThread}")
    private boolean multiThread;

    private ForkJoinPool forkJoinPool = new ForkJoinPool(20);

    /**
     * 登陆操作
     *
     * @param userName 用户名
     * @param passWord 密码
     * @return 登录信息
     */
    @Override
    public LoginDto login(String userName, String passWord) throws Exception {
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("用户名不能为空!");
        }
        if (StringUtils.isEmpty(passWord)) {
            throw new RuntimeException("密码不允许为空!");
        }

        if (aMoeBaSession.isLogin() && userName.equals(aMoeBaSession.getUserId()) && StringUtils.isNotEmpty(aMoeBaSession.getCookieStr())) {
            throw new Exception("请不要重复登陆！");
        } else {
            aMoeBaSession.login(userName, passWord);
        }
        LoginDto loginDto = new LoginDto();
        loginDto.setUserName(userName);
        loginDto.setPassWord(aMoeBaSession.getUserEmployeeId());
        loginDto.setUserEmployeeId(aMoeBaSession.getUserEmployeeId());
        loginDto.setUserEmployeeName(aMoeBaSession.getUserEmployeeName());
        LoginDto userInfo = sqliteDao.queryUserByPK(userName);
        if (userInfo == null) {
            sqliteDao.addUser(loginDto, "1");
        }
        return loginDto;
    }

    /**
     * 查询是否已经登录
     *
     * @return 登陆的信息
     */
    @Override
    public LoginDto queryLogin() throws Exception {
        LoginDto loginDto = new LoginDto();
        String userEmployeeId = aMoeBaSession.getUserEmployeeId();
        loginDto.setUserEmployeeId(userEmployeeId);
        String userEmployeeName = aMoeBaSession.getUserEmployeeName();
        loginDto.setUserEmployeeName(userEmployeeName);
        if (StringUtils.isEmpty(userEmployeeId)) {
            throw new Exception("未登录！");
        }
        loginDto.setUserName(userEmployeeName.split("-")[1]);
        return loginDto;
    }

    /**
     * 批量保存
     *
     * @param batchExecuteDto 保存数据
     * @return
     */
    @Override
    public String batchSave(BatchExecuteDto batchExecuteDto) throws Exception {
        if (batchExecuteDto == null) {
            throw new RuntimeException("入参不能为空！");
        }
        String amStartTime = batchExecuteDto.getAmStartTime();
        if (StringUtils.isEmpty(amStartTime)) {
            throw new RuntimeException("上午上班时间不能为空！");
        }
        String amEndTime = batchExecuteDto.getAmEndTime();
        if (StringUtils.isEmpty(amEndTime)) {
            throw new RuntimeException("上午下班时间不能为空！");
        }
        String pmStartTime = batchExecuteDto.getPmStartTime();
        if (StringUtils.isEmpty(pmStartTime)) {
            throw new RuntimeException("下午上班时间不能为空！");
        }
        String pmEndTime = batchExecuteDto.getPmEndTime();
        if (StringUtils.isEmpty(pmEndTime)) {
            throw new RuntimeException("下午下班时间不能为空！");
        }
        List<LogInfoDto> logInfoDtos = batchExecuteDto.getLogInfoDtos();
        if (logInfoDtos == null || logInfoDtos.size() < 1) {
            throw new RuntimeException("工作内容不能为空！");
        }
        for (LogInfoDto logInfoDto : logInfoDtos) {
            String startDateStr = logInfoDto.getStartDate();
            String endDateStr = logInfoDto.getEndDate();
            Boolean skipWeekend = logInfoDto.getSkipWeekend();
            String temp = startDateStr;
            List<String> holidays;
            holidays = sqliteDao.getHoliday(startDateStr, endDateStr);
            List<AMoeBaLogDto> aMoeBaLogDtos = new ArrayList<>();
            while (endDateStr.compareTo(temp) >= 0) {
                Date date = sdf.parse(temp);
                if (!holidays.contains(temp) && (!skipWeekend || !DateUtil.isWeekend(date))) {
                    LOGGER.info("{}开始组织{}日志数据", aMoeBaSession.getUserEmployeeName(), temp);
                    AMoeBaLogDto[] aMoeBaLogDto = getAMoeBaLogDto(batchExecuteDto, logInfoDto, temp);
                    aMoeBaLogDtos.add(aMoeBaLogDto[0]);
                    aMoeBaLogDtos.add(aMoeBaLogDto[1]);
                } else {
                    LOGGER.info("{}节假日或双休日跳过!", temp);
                }
                temp = sdf.format(DateUtil.calculateDate(DateUtil.DAY_OF_YEAR, sdf.parse(temp), 1));
            }
            if (multiThread) {
                ForkJoinTask<Boolean> res = forkJoinPool.submit(new AMoeBaExecuteTask(BaseRequestDto.sessionThreadLocal.get(), AMoeBaSession.ADD_ACTION, aMoeBaLogDtos.toArray(new AMoeBaLogDto[aMoeBaLogDtos.size()])));
                Boolean aBoolean = res.get();
                return aBoolean ? "success" : "error";
            } else {
                for (AMoeBaLogDto aMoeBaLogDto : aMoeBaLogDtos) {
                    aMoeBaSession.executeAMoeBaLog(aMoeBaLogDto, AMoeBaSession.ADD_ACTION);
                }
            }
        }
        return "success";
    }

    /**
     * 组织log数据，并填写
     *
     * @param batchExecuteDto
     * @param logInfoDto
     * @param ----aMoeBaLogDtos
     * @throws ParseException
     */
    private AMoeBaLogDto[] getAMoeBaLogDto(BatchExecuteDto batchExecuteDto, LogInfoDto logInfoDto, String startDate) throws ParseException, InterruptedException {
        AMoeBaLogDto amAMoeBaLogDto = new AMoeBaLogDto();
        amAMoeBaLogDto.setId(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
        amAMoeBaLogDto.setDateStart(DateUtil.sdf_en.parse(startDate + " " + batchExecuteDto.getAmStartTime() + ":00"));
        amAMoeBaLogDto.setDateEnd(DateUtil.sdf_en.parse(startDate + " " + batchExecuteDto.getAmEndTime() + ":00"));
        amAMoeBaLogDto.setText(logInfoDto.getContent());
        AMoeBaLogDto pmAMoeBaLogDto = new AMoeBaLogDto();
        pmAMoeBaLogDto.setId(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
        pmAMoeBaLogDto.setDateStart(DateUtil.sdf_en.parse(startDate + " " + batchExecuteDto.getPmStartTime() + ":00"));
        pmAMoeBaLogDto.setDateEnd(DateUtil.sdf_en.parse(startDate + " " + batchExecuteDto.getPmEndTime() + ":00"));
        pmAMoeBaLogDto.setText(logInfoDto.getContent());
        return new AMoeBaLogDto[]{amAMoeBaLogDto, pmAMoeBaLogDto};
    }

    /**
     * 按照日志id批量删除
     *
     * @param logIds 日志id集合
     * @return
     */
    @Override
    public String batchDelete(List<String> logIds) throws Exception {
        if (logIds == null || logIds.size() < 1) {
            throw new RuntimeException("日志id不能为空！");
        }
        if (aMoeBaSession.isDeleteing()) {
            throw new NullPointerException("还有未完成的删除操作，请稍后再试！");
        }
        try {
            aMoeBaSession.setDeleteing(true);
            if (multiThread) {
                AMoeBaLogDto[] aMoeBaLogDtos = new AMoeBaLogDto[logIds.size()];
                for (int i = 0; i < logIds.size(); i++) {
                    String logId = logIds.get(i);
                    aMoeBaLogDtos[i] = new AMoeBaLogDto(logId);
                }
                ForkJoinTask<Boolean> res = forkJoinPool.submit(new AMoeBaExecuteTask(BaseRequestDto.sessionThreadLocal.get(), AMoeBaSession.DELETE_ACTION, aMoeBaLogDtos));
                Boolean aBoolean = res.get();
                return aBoolean ? "success" : "error";
            } else {
                for (String logId : logIds) {
                    aMoeBaSession.executeAMoeBaLog(new AMoeBaLogDto(logId), AMoeBaSession.DELETE_ACTION);
                    LOGGER.info("{}:删除成功!", logId);
                }
            }
        } finally {
            aMoeBaSession.setDeleteing(false);
        }
        return "success";
    }

    /**
     * 查询指定时间的日志
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    @Override
    public Map<String, Collection<AMoeBaLogDto>> getLog(String startDate, String endDate) throws Exception {
        if (StringUtils.isEmpty(startDate)) {
            throw new RuntimeException("开始时间不能为空！");
        }
        if (StringUtils.isEmpty(endDate)) {
            throw new RuntimeException("结束时间不能为空！");
        }
        return aMoeBaSession.getAmoebaLogItems(startDate, endDate);
    }

    /**
     * 查询当年的日志
     *
     * @return
     */
    @Override
    public Map<String, Collection<AMoeBaLogDto>> getAllLog() throws Exception {
        return aMoeBaSession.getAmoebaLogItems();
    }

    /**
     * 此方法用于controller拦截器注入session信息
     *
     * @param aMoeBaSession
     */
    @Override
    public void session(AMoeBaSession aMoeBaSession) {
        this.aMoeBaSession = aMoeBaSession;
    }

    /**
     * 注销登陆
     *
     * @throws Exception
     */
    @Override
    public void logout() throws Exception {
        if (aMoeBaSession.isLogin() && StringUtils.isNotEmpty(aMoeBaSession.getCookieStr())) {
            String userEmployeeName = aMoeBaSession.getUserEmployeeName();
            sessionManager.freedSession(aMoeBaSession.getId());
            LOGGER.info("注销登录成功！{}", userEmployeeName);
        }
    }

    /**
     * 新增反馈信息
     *
     * @param feedBackDto 反馈信息
     * @return
     */
    @Override
    public int addFeedBack(FeedBackDto feedBackDto) throws Exception {
        if (feedBackDto == null) {
            throw new RuntimeException("反馈信息不能为空！");
        }
        String content = feedBackDto.getContent();
        if (StringUtils.isEmpty(content)) {
            throw new RuntimeException("反馈内容不能为空！");
        }
        String createBy = feedBackDto.getCreateBy();
        if (StringUtils.isEmpty(createBy)) {
            throw new RuntimeException("反馈人姓名不能为空！");
        }
        return sqliteDao.addFeedBack(feedBackDto);
    }

    /**
     * 校验用户是否有指定路径的访问权限
     *
     * @param userName  用户名
     * @param targetUrl 目标地址
     * @return
     */
    @Override
    public Boolean checkAccess(String userName, String targetUrl) throws Exception {
        if (StringUtils.isEmpty(userName)) {
            throw new RuntimeException("用户信息不能为空！");
        }
        if (StringUtils.isEmpty(targetUrl)) {
            throw new RuntimeException("目标路径不能为空！");
        }
        long sum = sqliteDao.checkAccess(userName, targetUrl);
        return sum > 0;
    }

    /**
     * 用于执行sql的服务，返回查询结果或者更新行数
     *
     * @param sql sql脚本
     * @return 查询结果或者更新行数
     * @throws Exception
     */
    @Override
    public Map<String, Object> sqlExecute(String sql) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            throw new RuntimeException("SQL不能为空！");
        }
        Map<String, Object> returnMap = new HashMap<>();
        sql = sql.toLowerCase().trim();
        if (sql.startsWith("select")) {
            returnMap.put("executeFlag", "query");
            QueryResultDto queryResultDto = sqliteDao.executeQuery(sql);
            returnMap.put("content", queryResultDto);
        } else if (sql.startsWith("insert into") || sql.startsWith("update") || sql.startsWith("delete")) {
            returnMap.put("executeFlag", "update");
            int i = sqliteDao.executeUpdate(sql);
            returnMap.put("content", i);
        } else {
            throw new SQLException("SQL语法错误！");
        }
        return returnMap;
    }

}
