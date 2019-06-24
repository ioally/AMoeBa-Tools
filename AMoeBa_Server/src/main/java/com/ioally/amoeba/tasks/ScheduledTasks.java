package com.ioally.amoeba.tasks;

import com.ioally.amoeba.session.SessionManager;
import com.ioally.amoeba.utils.jdbc.SqliteJdbc;
import com.ioally.amoeba.utils.other.DateUtil;
import com.ioally.amoeba.utils.other.Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

@Component
@Order(11)
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private SessionManager sessionManager;

    @Value("${amoeba.sessionTimeout}")
    private int sessionTimeout;

    @Autowired
    private SqliteJdbc sqliteJdbc;

    /**
     * 定时清理登录信息
     */
    @Scheduled(fixedRate = 1000)
    public synchronized void reportCurrentTime() {
        if (sessionManager != null) {
            sessionManager.freedSessionByTimeOut(sessionTimeout);
        }
    }

    /**
     * 查询当月是否有工作节假日
     *
     * @throws Exception
     */
    @Scheduled(cron = "0,30,50 0 0,5 * * ?")
    public void isHolidaysConfig() throws SQLException {
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        String dateStr = nowYear + Str.getCurNo((long) nowMonth, 2, "0");
        Connection connection = sqliteJdbc.getConnection();
        try {
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, "select count(1) from HolidayMain where holidayYear = '" + nowYear + "' and holidayId like '" + dateStr + "%'");
            DateUtil.isNowMonthHoliday = false;
            if (resultSet.next()) {
                long aLong = resultSet.getLong(1);
                DateUtil.isNowMonthHoliday = aLong > 0;
                LOGGER.info("{},包含工作节假日？{}", dateStr, DateUtil.isNowMonthHoliday);
            }
        } finally {
            connection.close();
        }

    }
}
