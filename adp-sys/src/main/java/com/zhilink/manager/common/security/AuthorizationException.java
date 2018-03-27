package com.zhilink.manager.common.security;

/**
 * 权限异常.
 * @author jaray
 *
 */
public class AuthorizationException extends CommonException {

    private static final long serialVersionUID = 2156586705179346077L;

    public AuthorizationException() {
        super("Request is not permitted");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
