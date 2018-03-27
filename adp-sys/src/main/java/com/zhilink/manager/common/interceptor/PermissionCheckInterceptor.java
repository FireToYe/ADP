package com.zhilink.manager.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zhilink.manager.common.security.AuthenticationException;
import com.zhilink.manager.common.security.Permitted;
import com.zhilink.manager.common.token.Token;
import com.zhilink.manager.common.token.TokenManager;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Constans;

/**
 * 权限拦截器.
 * 
 * @author jaray
 *
 */
public class PermissionCheckInterceptor extends HandlerInterceptorAdapter {

	public TokenManager tokenManager = null;

	@Autowired
	public void settokenManager(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// if(true)
		// return true;
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod method = (HandlerMethod) handler;
		Permitted anno = method.getMethodAnnotation(Permitted.class);
		if (anno == null) {
			return true;
		}

		String tokenkey = (String) request.getParameter(Constans.TOKENKEY);
		if (StringUtils.isEmpty(tokenkey)) {
			throw new AuthenticationException();
		}

		Token token = this.tokenManager.getToken(tokenkey);
		if (null == token) {
			throw new AuthenticationException();
		} else {
			this.tokenManager.addToken(token);
		}

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
