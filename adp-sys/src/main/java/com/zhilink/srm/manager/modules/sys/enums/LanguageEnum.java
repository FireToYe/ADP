package com.zhilink.srm.manager.modules.sys.enums;

import org.apache.commons.lang.StringUtils;

public enum LanguageEnum {

	/**
	 * 中文简体
	 */
	SIMPLIFIED_CHINESE("zh_CN", "ZhCN"),
	/*
	 * 台湾 繁体
	 */
	TRADITIONAL_CHINESE("zh_TW", "ZhTW"),
	/**
	 * 英文
	 */
	US("en_US", "EnUS"),;

	private String code;

	/**
	 * 实体属性名称后缀名 例如：Dict类,label的中文属性名称：labelZhCN=label+ZhCN
	 */
	private String fieldSuffix;

	public String getCode() {
		return code;
	}

	public String getFieldSuffix() {
		return fieldSuffix;
	}

	private LanguageEnum(String code, String fieldSuffix) {
		this.code = code;
		this.fieldSuffix = fieldSuffix;
	}

	/***
	 * 根据国际化编码获取实体国际化语言字段的后缀名
	 * 
	 * @param code
	 * @return
	 */
	public static String convertLanguageFieldSuffix(String code) {

		String ret = "";
		if (StringUtils.isBlank(code)) {
			return ret;
		}
		if (code.indexOf("-") >= 0) {
			code = code.replace("-", "_");
		}
		LanguageEnum[] values = LanguageEnum.values();
		for (int k = 0; k < values.length; k++) {
			if (code.equals(values[k].getCode())) {
				ret = values[k].getFieldSuffix();
			}
		}
		return ret;
	}
}
