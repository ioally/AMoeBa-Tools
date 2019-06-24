package com.ioally.amoeba.tasks;

import com.ioally.amoeba.dto.AMoeBaLogDto;
import com.ioally.amoeba.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.RecursiveTask;

/**
 * 使用ForkJoin框架，拆分任务，利用多线程将串行任务变为并行任务
 */
public class AMoeBaExecuteTask extends RecursiveTask<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AMoeBaExecuteTask.class);
    private AMoeBaLogDto[] aMoeBaLogDto;
    private String type;
    private Session session;
    private  int maxTaskNum = 2;

    public AMoeBaExecuteTask(Session session, String type, AMoeBaLogDto... aMoeBaLogDto) {
        if (aMoeBaLogDto == null || aMoeBaLogDto.length < 1) {
            throw new NullPointerException("待执行任务不可为空！");
        }
        if (session.getaMoeBaSession() == null) {
            throw new NullPointerException("无可用session会话！");
        }
        this.session = session;
        this.aMoeBaLogDto = aMoeBaLogDto;
        this.type = type;
    }

    @Override
    protected Boolean compute() {
        // 判断任务数量是否大于单线程可处理的最大任务数，
        // 如果不大于则直接执行任务并返回结果
        // 大于则拆分任务
        int taskLength = aMoeBaLogDto.length;
        if (taskLength <= maxTaskNum) {
            boolean success = true;
            for (AMoeBaLogDto moeBaLogDto : aMoeBaLogDto) {
                // 更新session时间，防止任务未执行完成session过期
                session.setLastTime(new Date());
                success &= session.getaMoeBaSession().executeAMoeBaLog(moeBaLogDto, type);
                LOGGER.info("线程：{}，使用ForkJoin执行阿米巴任务,任务类型:{},任务数据：{}", Thread.currentThread().getName(), type, moeBaLogDto);
            }
            return success;
        } else {
            LOGGER.debug("线程：{}，阿米巴ForkJoin任务过大（{}），进行拆分！", Thread.currentThread().getName(), taskLength);
            int index = taskLength / 2;
            // 将任务拆分成左右两部分
            AMoeBaLogDto[] left_aMoeBaLogDtos = Arrays.copyOfRange(aMoeBaLogDto, 0, index);
            AMoeBaLogDto[] right_aMoeBaLogDtos = Arrays.copyOfRange(aMoeBaLogDto, index, taskLength);
            AMoeBaExecuteTask left_aMoeBaExecuteTask = new AMoeBaExecuteTask(session, type, left_aMoeBaLogDtos);
            AMoeBaExecuteTask right_aMoeBaExecuteTask = new AMoeBaExecuteTask(session, type, right_aMoeBaLogDtos);
            left_aMoeBaExecuteTask.fork();
            right_aMoeBaExecuteTask.fork();
            Boolean left = left_aMoeBaExecuteTask.join();
            Boolean right = right_aMoeBaExecuteTask.join();
            return left & right;
        }
    }
}
