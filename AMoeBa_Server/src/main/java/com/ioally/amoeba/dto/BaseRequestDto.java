package com.ioally.amoeba.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ioally.amoeba.session.Session;

import java.io.Serializable;

/**
 * 请求基础数据的dto
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRequestDto implements Serializable {

    /**
     * 会话的sessionId
     */
    public static final ThreadLocal<String> sessionId = new ThreadLocal<>();

    public static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    static {
        sessionId.set("-1");
    }

    /**
     * 用户名id
     */
    protected String userEmployeeId;
    /**
     * 用户名-工号
     */
    protected String userEmployeeName;

    /**
     * 用户密钥
     */
    protected String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserEmployeeId() {
        return userEmployeeId;
    }

    public void setUserEmployeeId(String userEmployeeId) {
        this.userEmployeeId = userEmployeeId;
    }

    public String getUserEmployeeName() {
        return userEmployeeName;
    }

    public void setUserEmployeeName(String userEmployeeName) {
        this.userEmployeeName = userEmployeeName;
    }
}
