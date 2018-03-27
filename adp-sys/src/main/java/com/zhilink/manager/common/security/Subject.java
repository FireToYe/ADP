package com.zhilink.manager.common.security;

import java.util.Set;

/**
 * 用户容器.
 * @author jaray
 *
 */
public interface Subject<T> {

    boolean isPermitted(String perm);

    boolean hasRole(String role);

    Set<String> getPermissions();

    Set<String> getRoles();

    String getCurrentUserId();

    T getCurrentUser();

}
