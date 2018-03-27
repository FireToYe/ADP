package com.zhilink.srm.manager.modules.sys.listener;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;

import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.verify.VerifyLicense;

public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {

	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		if (!SystemService.printKeyLoadMessage()) {
			return null;
		}
		return super.initWebApplicationContext(servletContext);
	}
}
