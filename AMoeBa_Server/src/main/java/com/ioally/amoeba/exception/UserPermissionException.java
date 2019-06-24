package com.ioally.amoeba.exception;

/**
 * 用户权限异常，基础控制器校验无权限时会抛出此异常
 */
public class UserPermissionException extends Exception {

    public UserPermissionException() {
    }

    public UserPermissionException(String message) {
        super(message);
    }
}
