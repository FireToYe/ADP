package com.zhilink.manager.common.utils;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zhilink.manager.common.web.ReloadableResourceBundleMessageSourceCust;
import com.zhilink.manager.framework.common.utils.SpringContextHolder;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.manager.modules.sys.enums.LanguageEnum;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 国际化语言工具包
 * 
 * @author an48huf
 * @date 2017年9月11日
 */
@Component
public class I18n {

	private static ReloadableResourceBundleMessageSourceCust i18nMessageSource = SpringContextHolder
			.getBean(ReloadableResourceBundleMessageSourceCust.class);

	public final static String LANGKEY = "lang";

	public static String convert(int code) {
		return i18nMessage(String.valueOf(code));
	}

	/**
	 * 根据编码获取国际化语言
	 * 
	 * @param code
	 *            编码key
	 * @return
	 */
	public static String i18nMessage(String code) {
		String ret = "";
		if (StringUtils.isBlank(code)) {
			return ret;
		}
		return i18nMessage(code, getCurrenLocale());

	}

	/**
	 * 根据编码获取国际化语言
	 * 
	 * @param code
	 *            编码key
	 * @param args
	 *            参数列表
	 * @return
	 */
	public static String i18nMessage(String code, Object[] args) {
		String ret = "";
		if (StringUtils.isBlank(code)) {
			return ret;
		}
		return i18nMessage(code, args, getCurrenLocale());
	}

	/**
	 * 获取编码和Locale区域信息获取国际化语言
	 * 
	 * @param code
	 *            编码
	 * @param locale
	 *            地点区域信息
	 * @return
	 */
	public static String i18nMessage(String code, Locale locale) {
		String ret = "";
		if (StringUtils.isBlank(code)) {
			return ret;
		}
		return i18nMessage(code, null, locale);
	}

	/**
	 * 获取编码和Locale区域信息获取国际化语言
	 * 
	 * @param code
	 *            编码
	 * @param args
	 *            参数列表
	 * @param locale
	 *            地点区域信息
	 * @return
	 */
	public static String i18nMessage(String code, Object[] args, Locale locale) {

		String ret = "";
		if (StringUtils.isBlank(code)) {
			return ret;
		}
		try {
			ret = i18nMessageSource.getMessage(code.trim(), args, locale);
		} catch (Exception e) {

		}
		return ret;
	}

	private static String getLocaleFromCookie(HttpServletRequest request, String name) {

		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return "";
	}

	private static Locale getCurrenLocale() {
		Locale locale = null;
		// session中获取语言
		Session session = UserUtils.getSession();
		if(session!=null){
			Object langObj = session.getAttribute(LANGKEY);
			if (langObj != null) {
				locale = (Locale) langObj;
			}
		}
		// 从cookie中获取
		if (locale == null) {
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attrs != null) {
				String localeStr = getLocaleFromCookie(attrs.getRequest(), LANGKEY);
				if (StringUtils.isNotBlank(localeStr)) {
					locale = org.springframework.util.StringUtils.parseLocaleString(localeStr);
				}
			}
		}
		if (locale == null) {
			locale = org.springframework.util.StringUtils.parseLocaleString("zh_CN");//默认中文
		}
		return locale;
	}

	/**
	 * 获取当前会话的语言
	 * 
	 * @return
	 */
	public static String getCurrentLanguage() {
		return getCurrenLocale().toString();
	}

	/**
	 * 根据国际化编码获取实体国际化语言字段的后缀名
	 * 
	 * @param lang
	 *            语言编码
	 * @return
	 */
	public static String convertLanguageFieldSuffix(String lang) {
		return LanguageEnum.convertLanguageFieldSuffix(lang);
	}
}
