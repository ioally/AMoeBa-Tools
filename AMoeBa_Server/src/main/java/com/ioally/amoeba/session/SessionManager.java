package com.ioally.amoeba.session;

import com.ioally.amoeba.dto.BaseRequestDto;
import com.ioally.amoeba.exception.OutOfSessionException;
import com.ioally.amoeba.utils.other.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 会话管理类
 */
public final class SessionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);

    private final static ReadWriteLock LOCK = new ReentrantReadWriteLock();

    // 会话连接池
    private final static Map<String, Session> SESSION_POOL = new HashMap<>();

    private static SessionManager sessionManager;

    private String address;

    private SessionManager(String address) {
        this.address = address;
    }

    /**
     * 初始化sessionPool，不允许创建多个
     *
     * @param address  地址
     * @param poolSize 池大小
     * @return SessionManager
     */
    public static SessionManager newInstance(String address, int poolSize) {
        if (sessionManager == null) {
            synchronized (LOCK) {
                if (sessionManager == null) {
                    sessionManager = new SessionManager(address);
                    sessionManager.init(poolSize);
                }
            }
        }
        return sessionManager;
    }

    /**
     * 获取一个Session实例
     *
     * @return Session
     */
    public Session getSession() throws Exception {
        try {
            LOCK.readLock().lock();
            if (SESSION_POOL.size() > 0) {
                Set<String> aMeoBaSessionIds = SESSION_POOL.keySet();
                for (String aMeoBaSessionId : aMeoBaSessionIds) {
                    Session session = SESSION_POOL.get(aMeoBaSessionId);
                    if (session != null
                            && (BaseRequestDto.sessionId.get().equals(session.getSessionId()) || session.isActive())) {
                        session.setActive(false);
                        if (session.getLastTime() == null) {
                            session.setLastTime(new Date());
                        }
                        return session;
                    }
                }
                throw new OutOfSessionException("暂时无可用会话连接，请稍后再试！");
            } else {
                throw new Exception("SessionPool为空，请初始化！");
            }
        } finally {
            LOCK.readLock().unlock();
        }
    }


    /**
     * 初始化session池
     *
     * @param poolSize 池的大小
     */
    private void init(int poolSize) {
        try {
            LOCK.writeLock().lock();
            if (SESSION_POOL.isEmpty() && poolSize > 0) {
                for (int i = 0; i < poolSize; i++) {
                    Session session = SessionFactory.newAMoeBaSessionInstance(address);
                    SESSION_POOL.put(session.getId(), session);
                }
            }
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /**
     * 释放一个session
     */
    public void freedSession(String aMoeBaSessionId) {
        try {
            LOCK.writeLock().lock();
            if (SESSION_POOL.containsKey(aMoeBaSessionId)) {
                Session session = SESSION_POOL.get(aMoeBaSessionId);
                session.getaMoeBaSession().clear();
                session.setSessionId(null);
                session.setActive(true);
                session.setLastTime(null);
            }
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /**
     * 释放一个超时的session
     *
     * @param timeOut 超时时间
     */
    public void freedSessionByTimeOut(int timeOut) {
        try {
            LOCK.writeLock().lock();
            Set<String> keySet = SESSION_POOL.keySet();
            for (String sid : keySet) {
                Session session = SESSION_POOL.get(sid);
                if (session != null && session.getLastTime() != null) {
                    if (DateUtil.isTimeout(session.getLastTime(), timeOut)) {
                        this.freedSession(session.getId());
                        LOGGER.info("释放超时的AMoeBaSession,id=[{}]", session.getId());
                    }
                }
            }
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /**
     * 释放一个超时的session
     *
     * @param timeOut 超时时间
     */
    public void freedSessionByTimeOut(String ameobaId, int timeOut) {
        try {
            LOCK.writeLock().lock();
            Session session = SESSION_POOL.get(ameobaId);
            if (session != null && session.getLastTime() != null) {
                if (DateUtil.isTimeout(session.getLastTime(), timeOut)) {
                    this.freedSession(session.getId());
                    LOGGER.info("释放超时的AMoeBaSession,id=[{}]", session.getId());
                }
            }
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    /**
     * 重置sessionPool
     */
    public void rest() {
        try {
            LOCK.writeLock().lock();
            if (!SESSION_POOL.isEmpty()) {
                Iterator<String> aMoeBaSessionIds = SESSION_POOL.keySet().iterator();
                while (aMoeBaSessionIds.hasNext()) {
                    String aMoeBaSessionId = aMoeBaSessionIds.next();
                    Session session = SESSION_POOL.get(aMoeBaSessionId);
                    session.getaMoeBaSession().clear();
                    aMoeBaSessionIds.remove();
                }
            }
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}
