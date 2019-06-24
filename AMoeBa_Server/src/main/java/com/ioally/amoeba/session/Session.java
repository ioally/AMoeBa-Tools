package com.ioally.amoeba.session;

import java.util.Date;

public class Session {

    private String id;

    private String sessionId;

    private boolean active = true;

    private Date lastTime;

    private AMoeBaSession aMoeBaSession;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public AMoeBaSession getaMoeBaSession() {
        return aMoeBaSession;
    }

    public void setaMoeBaSession(AMoeBaSession aMoeBaSession) {
        this.aMoeBaSession = aMoeBaSession;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}
