/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.zhilink.srm.common.persistence.DataEntity;

/**
 * app授权申请表Entity
 * @author chrisye
 * @version 2017-09-28
 */
public class AppApply extends DataEntity<AppApply> {
	
	private static final long serialVersionUID = 1L;
	private String openId;		// openid
	private String appId;		// appid
	private String applyInfo;		// 申请信息(json)
	private String status;		// 状态(0-默认，1-已审核，2-审核不通过)
	private String relateUser; //关联用户
	@JSONField(serialize=false)
	private Map<String, Object> objApplyInfo;
	@JSONField(serialize=false)
	private List<String> applyInfoSearch;
	
	
	public AppApply() {
		super();
	}

	public AppApply(String id){
		super(id);
	}

	@Length(min=1, max=32, message="openid长度必须介于 1 和 32 之间")
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	@Length(min=1, max=32, message="appid长度必须介于 1 和 32 之间")
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	@Length(min=0, max=2000, message="申请信息(json)长度必须介于 0 和 2000 之间")
	public String getApplyInfo() {
		return applyInfo;
	}

	public void setApplyInfo(String applyInfo) {
		this.applyInfo = applyInfo;
		if(applyInfo!=null && !"".equals(applyInfo.trim()) && applyInfo.trim().startsWith("{")){
			this.objApplyInfo=JSONObject.parseObject(applyInfo);
		}
	}
	
	@Length(min=1, max=4, message="状态(0-默认，1-已审核，2-审核不通过)长度必须介于 1 和 4 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRelateUser() {
		return relateUser;
	}

	public void setRelateUser(String relateUser) {
		this.relateUser = relateUser;
	}
	
	public Map<String, Object> getObjApplyInfo() {
		return objApplyInfo;
	}

	public void setObjApplyInfo(Map<String, Object> objApplyInfo) {
		this.objApplyInfo = objApplyInfo;
	}

	public List<String> getApplyInfoSearch() {
		return applyInfoSearch;
	}

	public void setApplyInfoSearch(List<String> applyInfoSearch) {
		this.applyInfoSearch = applyInfoSearch;
	}
	
}