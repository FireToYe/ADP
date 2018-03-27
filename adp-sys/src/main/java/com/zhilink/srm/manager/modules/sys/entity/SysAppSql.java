/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 应用SQL管理Entity
 * @author xh
 * @version 2017-12-12
 */
public class SysAppSql extends DataEntity<SysAppSql> {
	
	private static final long serialVersionUID = 1L;
	private String appname;		// 应用项目名称
	private String version;		// 当前版本
	private String sqltext;		// sql脚本
	private String rollbacktext;		// 反向sql脚本
	private String sqlerror;		// 出错sql脚本
	
	public SysAppSql() {
		super();
	}

	public SysAppSql(String id){
		super(id);
	}

	@Length(min=1, max=50, message="应用项目名称长度必须介于 1 和 50 之间")
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
	
	public String getSqltext() {
		return sqltext;
	}

	public void setSqltext(String sqltext) {
		this.sqltext = sqltext;
	}
	
	public String getRollbacktext() {
		return rollbacktext;
	}

	public void setRollbacktext(String rollbacktext) {
		this.rollbacktext = rollbacktext;
	}
	
	public String getSqlerror() {
		return sqlerror;
	}

	public void setSqlerror(String sqlerror) {
		this.sqlerror = sqlerror;
	}
	
}