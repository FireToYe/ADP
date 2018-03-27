package com.zhilink.srm.manager.modules.sys.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: DataScopeEnum
 * @Description: 数据权限枚举
 * @author Administrator
 * @date 2017年11月20日
 *
 */
public enum DataScopeEnum {

	DATA_SCOPE_ALL("1", "所有数据"), DATA_SCOPE_COMPANY_AND_CHILD("2", "所在公司及以下数据"), DATA_SCOPE_COMPANY("3", "所在公司数据"),

	DATA_SCOPE_OFFICE_AND_CHILD("4", "所在部门及以下数据"),

	DATA_SCOPE_OFFICE("5", "所在部门数据"), DATA_SCOPE_SELF("8", "仅本人数据"), DATA_SCOPE_CUSTOM("9", "按明细设置"),

	;

	private String value;

	private String showName;

	private DataScopeEnum(String value, String showName) {
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
	 * 返回数据权限的map集合
	 * 
	 * @return
	 */
	public static List dataScopeToList() {
		DataScopeEnum[] values = DataScopeEnum.values();
		List<Map> dataScopeList=new ArrayList<Map>();
		if (values != null && values.length > 0) {
			for (int k = 0; k < values.length; k++) {
				if (StringUtils.isNotBlank(values[k].getValue())) {		
					Map<String, String> dataScopeMap = new HashMap<String, String>();
					dataScopeMap.put("value", values[k].getValue());
					dataScopeMap.put("showName", values[k].getShowName());
					dataScopeList.add(dataScopeMap);
				}
			}
		}
		return dataScopeList;
	}

}
