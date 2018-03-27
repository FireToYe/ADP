package com.zhilink.manager.common.security;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全管理器.
 * @author jaray
 *
 */
public interface SecurityManager<T> {

    Subject<T> getSubject(HttpServletRequest request);

    void login(HttpServletRequest request, String userId, Set<String> permissions, Set<String> roles, String...args);

    void logout(HttpServletRequest request);

}
