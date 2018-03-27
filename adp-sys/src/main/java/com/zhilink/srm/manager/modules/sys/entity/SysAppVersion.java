/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 应用版本管理Entity
 * @author keris
 * @version 2017-10-13
 */
public class SysAppVersion extends DataEntity<SysAppVersion> {
	
	private static final long serialVersionUID = 1L;
	private String appname;		// 应用项目名称
	private String version;		// 当前版本
	private Date uptime;		// 升级时间
	private String newversion;  //升级版本
	public String getNewversion() {
		return newversion;
	}

	public void setNewversion(String newversion) {
		this.newversion = newversion;
	}

	public SysAppVersion() {
		super();
	}

	public SysAppVersion(String id){
		super(id);
	}

	@Length(min=1, max=255, message="应用项目名称长度必须介于 1 和 255 之间")
	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	@Length(min=1, max=20, message="当前版本长度必须介于 1 和 20 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUptime() {
		return uptime;
	}

	public void setUptime(Date uptime) {
		this.uptime = uptime;
	}
	
}