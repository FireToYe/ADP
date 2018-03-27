package com.zhilink.srm.manager.modules.sys.entity;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;


/**
 * 扩展字段Entity
 * @version 2017-12-20
 */
public class SysExtendWord extends DataEntity<SysExtendWord> {
	
	private static final long serialVersionUID = 1L;
	private String name; //数据库里的字段名
	private String displayName; //前端页面显示的字段名
	private String type; // 数据库里的字段类型
	private Integer sort; //排序
	private String key; //对应的接口名称
	private String comments; //对key的中文描述
	
	private String displayNameCN; //前端页面显示的中文名称
	private String displayNameTW; //前端页面显示的繁体名称
	private String displayNameUN; //前端页面显示的英文名称
	
	@Length(min=0, max=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Length(min=0, max=100)
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@NotNull
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@Length(min=0, max=50)
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDisplayNameCN() {
		return displayNameCN;
	}
	public void setDisplayNameCN(String displayNameCN) {
		this.displayNameCN = displayNameCN;
	}
	public String getDisplayNameTW() {
		return displayNameTW;
	}
	public void setDisplayNameTW(String displayNameTW) {
		this.displayNameTW = displayNameTW;
	}
	public String getDisplayNameUN() {
		return displayNameUN;
	}
	public void setDisplayNameUN(String displayNameUN) {
		this.displayNameUN = displayNameUN;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getKeyAndComments(){
		return getKey() + (comments == null ? "" : "  :  " + comments);
	}
}