package com.ioally.amoeba.init;

import com.ioally.amoeba.session.SessionManager;
import com.ioally.amoeba.utils.jdbc.SqliteJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;

@Component
@Order(10)
public class SessionManagerBean {

    @Value("${amoeba.url}")
    private String aMoeBaUrl;

    @Value("${amoeba.sessionSize}")
    private int sessionPoolSize;

    @Autowired
    private SqliteJdbc sqliteJdbc;

    @Bean("sessionManager")
    public SessionManager sessionManager() {
        return SessionManager.newInstance(aMoeBaUrl, sessionPoolSize);
    }

    @Bean("isHolidaysConfig")
    public boolean isHolidaysConfig() throws Exception {
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        boolean isHolidaysConfig = false;
        Connection connection = sqliteJdbc.getConnection();
        try {
            ResultSet resultSet = sqliteJdbc.executeQuery(connection, "select count(1) from HolidayMain where holidayYear = '" + nowYear + "'");
            if (resultSet.next()) {
                long aLong = resultSet.getLong(1);
                isHolidaysConfig = aLong > 0;
            }
        } finally {
            connection.close();
        }
        return isHolidaysConfig;
    }
}
