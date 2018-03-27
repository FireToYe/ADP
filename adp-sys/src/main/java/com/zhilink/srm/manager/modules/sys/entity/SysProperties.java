/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 系统参数Entity
 * @author chrisye
 * @version 2018-01-16
 */
public class SysProperties extends DataEntity<SysProperties> {
	
	private static final long serialVersionUID = 1L;
	private String key;		// 字段key
	private String value;		// 字段value
	private String modifyBy;		// 是否可手动修改
	
	public SysProperties() {
		super();
	}

	public SysProperties(String id){
		super(id);
	}

	public SysProperties(String key, String value, String modifyBy) {
		super();
		this.key = key;
		this.value = value;
		this.modifyBy = modifyBy;
	}

	@Length(min=1, max=200, message="字段key长度必须介于 1 和 200 之间")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Length(min=0, max=11, message="是否可手动修改长度必须介于 0 和 11 之间")
	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	
}