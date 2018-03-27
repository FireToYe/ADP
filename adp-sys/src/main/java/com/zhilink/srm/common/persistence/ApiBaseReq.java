package com.zhilink.srm.common.persistence;

public class ApiBaseReq {
	
	private String ent;                 // 集团
	private String site;                // 据点
	private String lang;                 //语言别
	private String appmodule;         // 模组
	private String timestamp;            //时间戳
	
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
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getAppmodule() {
		return appmodule;
	}
	public void setAppmodule(String appmodule) {
		this.appmodule = appmodule;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
