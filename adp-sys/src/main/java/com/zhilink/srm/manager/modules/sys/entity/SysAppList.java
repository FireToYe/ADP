/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * app清单表Entity
 * @author chrisye
 * @version 2017-09-28
 */
public class SysAppList extends DataEntity<SysAppList> {
	
	private static final long serialVersionUID = 1L;
	private String appId;		// appid
	private String appSecret;		// app_secret
	private String token;		// token
	private String redirect;		// 跳转连接
	private String appName;		// app名称
	private String appType;		// 类型(1-微信,2-QQ,3-微博)
	private String infoKeys;		// 申请内容字段(json)
	
	public SysAppList() {
		super();
	}

	public SysAppList(String id){
		super(id);
	}

	@Length(min=1, max=32, message="appid长度必须介于 1 和 32 之间")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@Length(min=1, max=32, message="app_secret长度必须介于 1 和 32 之间")
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	@Length(min=1, max=32, message="token长度必须介于 1 和 32 之间")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Length(min=0, max=255, message="跳转连接长度必须介于 0 和 255 之间")
	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
	
	@Length(min=1, max=255, message="app名称长度必须介于 1 和 255 之间")
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	@Length(min=1, max=4, message="类型(1-微信,2-QQ,3-微博)长度必须介于 1 和 4 之间")
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
	
	@Length(min=0, max=2000, message="申请内容字段(json)长度必须介于 0 和 2000 之间")
	public String getInfoKeys() {
		return infoKeys;
	}

	public void setInfoKeys(String infoKeys) {
		this.infoKeys = infoKeys;
	}
	
}