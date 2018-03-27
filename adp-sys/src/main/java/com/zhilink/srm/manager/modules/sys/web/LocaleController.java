package com.zhilink.srm.manager.modules.sys.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhilink.manager.framework.common.utils.CookieUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/sys/locale")
public class LocaleController extends BaseController {

	@Autowired
	private SystemService systemService;
	private final static String LANGKEY = "lang";

	/**
	 * 国际化语言切换
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/change")
	public String changeLanguage(HttpServletRequest request, HttpServletResponse response) {

		String newLocale = request.getParameter(LANGKEY);
		Locale curLocale = org.springframework.util.StringUtils.parseLocaleString(newLocale);
		if (curLocale != null) {
			User user = UserUtils.getUser();
			if (user != null) {
				// 更新缓存
				user.setLanguage(curLocale.toString());
				// 保存当前用户的语言
				systemService.updateLangById(user.getId(), user.getLanguage());
				// 缓存到session
				UserUtils.getSession().setAttribute(LANGKEY, curLocale);
			}
			// 输出cookie,记录语言
			CookieUtils.setCookie(response, LANGKEY, curLocale.toString());

		}
		return "redirect:" + request.getParameter("url");
	}

}
