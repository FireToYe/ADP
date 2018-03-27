package com.zhilink.srm.manager.modules.sys.enums;

import java.util.List;

import com.google.common.collect.Lists;

/***
 * @ClassName: UserTypeEnum
 * @Description: 用户类型，一级组织管理员：当前用户的一级组织架构的系统管理员，只能分配当前用户所在的一级组织架构的权限
 * @author an48huf
 * @date 2017年11月17日
 *
 */
public enum UserTypeEnum {

	/** 系统管理 **/
	SYSTEMUSER("1"),
	/** 部门经理 **/
	DEPARTMENTMANAGER("2"),
	/** 普通用户 **/
	GENERALUSER("3"),
	/** 一级组织管理员 **/
	LEVELONEORGMANAGER("4"),

	;

	private String value;

	private UserTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static List<String> valuesToList() {

		List<String> list = Lists.newArrayList();
		UserTypeEnum[] values = UserTypeEnum.values();
		if (values != null && values.length > 0) {
			for (int k = 0; k < values.length; k++) {
				list.add(values[k].getValue());
			}
		}
		return list;
	}

}
