package com.ioally.amoeba.init;

import com.ioally.amoeba.tasks.ScheduledTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * 自启动操作
 */
@Component
@Order(12)
public class InitRunner implements ApplicationRunner {

    @Autowired
    private ScheduledTasks scheduledTasks;

    @Override
    public void run(ApplicationArguments args) throws SQLException {
        scheduledTasks.isHolidaysConfig();
    }
}
