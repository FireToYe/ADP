package com.zhilink.srm.manager.modules.sys.security;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

public class UserLoginFilter extends AccessControlFilter {

	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

		if (isLoginRequest(request, response)) {
			return true;
		} else {
			Subject subject = getSubject(request, response);
			// If principal is not null, then the user is known and should be allowed
			// access.
			return subject.getPrincipal() != null;
		}
	}

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		saveRequest(request);

		HttpServletRequest re = (HttpServletRequest) request;
		String loginUrl = getLoginUrl();
		loginUrl = re.getContextPath() + loginUrl;

		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open ('" + loginUrl + "','_top')");
		out.println("</script>");
		out.println("</html>");
		out.flush();
		out.close();
		return false;
	}
}
