package com.zhilink.srm.manager.modules.sys.security;

import java.io.Serializable;
import java.math.BigDecimal;

public class AppBase implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private String timestamp;// 时间戳
	private String appmodule;// 模组号
	private String appid;// 设备码
	private String appversion;// app版本
	
	private String lang;

	private String ent;

	private String site;
    
//	public AppBase(AppBase base ) {
//		super();
//		this.timestamp = base.getTimestamp();
//		this.appmodule = base.getAppmodule();
//		this.appid = base.getAppid();
//		this.appversion = base.getAppversion();
//		this.lang = base.getLang();
//		this.ent = base.getEnt();
//		this.site = base.getSite();
//	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getEnt() {
		return ent;
	}

	public void setEnt(String ent) {
		this.ent = ent;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAppmodule() {
		return appmodule;
	}

	public void setAppmodule(String appmodule) {
		this.appmodule = appmodule;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

}
