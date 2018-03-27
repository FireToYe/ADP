package com.zhilink.manager.common.security;

/**
 * 用户认证异常.
 * @author jaray
 *
 */
public class AuthenticationException extends CommonException {
     
	public static  final String ERROR_MSG="登录已过期或未登录";
	
    private static final long serialVersionUID = 2139177956508243331L;

    public AuthenticationException() {
        super("User is not Authenticated");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
