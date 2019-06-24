package com.ioally.amoeba.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SessionFactory {

    private SessionFactory() {
    }

    /**
     * 获取AMoeBaSession实例
     *
     * @param address 地址
     * @return
     */
    public static Session newAMoeBaSessionInstance(String address) {
        Session session = new Session();
        AMoeBaSession aMoeBaSession = new AMoeBaSession(address);
        session.setaMoeBaSession(aMoeBaSession);
        session.setId(aMoeBaSession.getId());
        session.setActive(true);
        return session;
    }

}
