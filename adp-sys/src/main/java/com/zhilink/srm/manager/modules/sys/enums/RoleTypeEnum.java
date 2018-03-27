package com.zhilink.srm.manager.modules.sys.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: RoleTypeEnum
 * @Description: 角色类型,公共角色可以给任何管理员授权使用
 * @author an48huf
 * @date 2017年11月17日
 *
 */
public enum RoleTypeEnum {

	/** 任务分配 **/
	ASSIGNMENT("assignment", "任务分配"),
	/** 管理角色 **/
	SECURITYROLE("security-role", "管理角色"),
	/** 普通角色 **/
	USER("user", "普通角色"),
	/** 公共角色 **/
	PUBLICROLE("public-role", "公共角色"),;

	private String value;

	private String showName;

	private RoleTypeEnum(String value, String showName) {
		this.value = value;
		this.showName = showName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	/**
	 * 获取角色类型的map集合
	 * 
	 * @return
	 */
	public static List roleTypeToList() {
		RoleTypeEnum[] values = RoleTypeEnum.values();
		List<Map> roleTypeList=new ArrayList<Map>();
		if (values != null && values.length > 0) {
			for (int k = 0; k < values.length; k++) {
				if (StringUtils.isNotBlank(values[k].getValue())) {
					Map<String, String> roleTypeMap = new HashMap<String, String>();
					roleTypeMap.put("value",values[k].getValue());
					roleTypeMap.put("showName",values[k].getShowName());
					roleTypeList.add(roleTypeMap);
				}
			}
		}
		return roleTypeList;
	}

	public static String getRoleTypeName(String value) {
		String ret = "";
		if (StringUtils.isBlank(value)) {
			return ret;
		}
		RoleTypeEnum[] values = RoleTypeEnum.values();
		if (values != null && values.length > 0) {
			for (int k = 0; k < values.length; k++) {
				if (value.trim().toLowerCase().equals(values[k].getValue())) {
					return values[k].getShowName();
				}
			}
		}
		return ret;
	}
}
