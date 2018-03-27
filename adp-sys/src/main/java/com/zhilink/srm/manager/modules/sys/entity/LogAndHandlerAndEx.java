package com.zhilink.srm.manager.modules.sys.entity;

import java.util.Map;

public class LogAndHandlerAndEx {
	private Log log;
	private Object handler;
	private Exception ex;
	private Map<String,Object> model;
	private String contextPath;
	public Map<String, Object> getModel() {
		return model;
	}
	public void setModel(Map<String, Object> model) {
		this.model = model;
	}
	public Log getLog() {
		return log;
	}
	public void setLog(Log log) {
		this.log = log;
	}
	public Object getHandler() {
		return handler;
	}
	public void setHandler(Object handler) {
		this.handler = handler;
	}
	public Exception getEx() {
		return ex;
	}
	public void setEx(Exception ex) {
		this.ex = ex;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
}
