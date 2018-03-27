/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.CookieUtils;
import com.zhilink.manager.framework.common.utils.IdGen;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.exception.AccessDeniedException;
import com.zhilink.srm.common.security.shiro.session.SessionDAO;
import com.zhilink.srm.common.servlet.ValidateCodeServlet;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.security.FormAuthenticationFilter;
import com.zhilink.srm.manager.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * 
 * @author jaray
 * 
 */
@Controller
public class LoginController extends BaseController {

	@Autowired
	private SessionDAO sessionDAO;
	// 密码输入错误上限
	private static final int PWD_ERROR_COUNT = 5;
	// 密码输入错误解锁时间，分钟
	private static final int PWD_UNLOCK_TIME = 3;

	private static final String CUSTOMER_ICON_FLAG = "zhil-";
	/**
	 * api 登录
	 */
	// @RequestMapping(value = "${apiPath}/login", method = RequestMethod.GET)
	// @ResponseBody
	// public ResultModel apiLogin(String loginName, String password, String vCode)
	// {
	// ResultModel result = new ResultModel();
	// // 校验验证码 直接pass
	// if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
	// result.setErrorCode(Messages.LOGIN_EXCEPTION_CODE);
	// result.setErrorMsg(Messages.LOGIN_EXCEPTION_USERERROR);
	// return result;
	// }
	//
	// Subject currentUser = UserUtils.getSubject();
	// if (!currentUser.isAuthenticated()) {
	// UsernamePasswordToken token = new UsernamePasswordToken(loginName, password,
	// LoginType.API);
	// try {
	// currentUser.login(token);
	// } catch (Exception ex) {// login failure
	// throw new CommonException(Messages.LOGIN_EXCEPTION_CODE,
	// Messages.LOGIN_EXCEPTION_USERERROR);
	// }
	// }
	// User user = UserUtils.getUser();
	// Map<String, Object> body = new HashMap<String, Object>();
	// body.put("userInfo", user);
	// // api 使用token 代替seesion
	// body.put(Constants.TOKEN, UserUtils.getSession().getId());
	// System.out.println(UserUtils.getSession().getId());
	// result.setBody(body);
	// return result;
	// }

	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		// API fix
		if (Global.isApiRequest(request)) {

			// api 的方式 直接抛出异常
			throw new AccessDeniedException();

		}

		// // 默认页签模式
		// String tabmode = CookieUtils.getCookie(request, "tabmode");
		// if (tabmode == null){
		// CookieUtils.setCookie(response, "tabmode", "1");
		// }

		if (logger.isDebugEnabled()) {
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return "redirect:" + adminPath;
		}
		// String view;
		// view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
		// view = "classpath:";
		// view +=
		// "jar:file:/D:/GitHub/zhilink/src/main/webapp/WEB-INF/lib/zhilink.jar!";
		// view += "/"+getClass().getName().replaceAll("\\.",
		// "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
		// view += ".jsp";
		return "modules/sys/sysLogin";
	}

	/**
	 * 用户注销，清空用户缓存
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "${adminPath}/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {

		UserUtils.clearCache();
		try {
			UserUtils.getSubject().logout();
		} catch (SessionException ise) {
			logger.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
		}
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();

		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			return "redirect:" + adminPath;
		}

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

		if (logger.isDebugEnabled()) {
			logger.debug("login fail, active session size: {}, message: {}, exception: {}",
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}

		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)) {
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		}

		// 验证失败清空验证码
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());

		// 如果是手机登录，则返回JSON字符串
		if (mobile) {
			return renderString(response, model);
		}

		return "modules/sys/sysLogin";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		User user = UserUtils.getUser();
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);

		if (logger.isDebugEnabled()) {
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		if (StringUtils.isNotBlank(user.getLanguage())) {
			// 缓存到session
			UserUtils.getSession().setAttribute(I18n.LANGKEY,
					org.springframework.util.StringUtils.parseLocaleString(user.getLanguage()));
			// 输出cookie,记录语言
			CookieUtils.setCookie(response, I18n.LANGKEY, user.getLanguage());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)) {
				CookieUtils.setCookie(response, "LOGINED", "true");
			} else if (StringUtils.equals(logined, "true")) {
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}

		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()) {
			if (request.getParameter("login") != null) {
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null) {
				model.addAttribute("menuTree", treeHtml());
				return "modules/sys/sysIndex";
			}
			return "redirect:" + adminPath + "/login";
		}

		// // 登录成功后，获取上次登录的当前站点ID
		// UserUtils.putCache("siteId",
		// StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));

		// System.out.println("==========================a");
		// try {
		// byte[] bytes =
		// com.zhilink.srm.common.utils.FileUtils.readFileToByteArray(
		// com.zhilink.srm.common.utils.FileUtils.getFile("c:\\sxt.dmp"));
		// UserUtils.getSession().setAttribute("kkk", bytes);
		// UserUtils.getSession().setAttribute("kkk2", bytes);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//// for (int i=0; i<1000000; i++){
		//// //UserUtils.getSession().setAttribute("a", "a");
		//// request.getSession().setAttribute("aaa", "aa");
		//// }
		// System.out.println("==========================b");
		model.addAttribute("menuTree", treeHtml());
		return "modules/sys/sysIndex";
	}

	@RequestMapping(value = "${adminPath}/welcome")
	public String welcome() {
		return "modules/sys/welcome";
	}

	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		if (StringUtils.isNotBlank(theme)) {
			CookieUtils.setCookie(response, "theme", theme);
		} else {
			theme = CookieUtils.getCookie(request, "theme");
		}
		model.addAttribute("menuTree", treeHtml());
		return "redirect:" + request.getParameter("url");
	}

	/**
	 * 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
		Map<String, Long> loginFailMap = (Map<String, Long>) CacheUtils.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = null;
		if (loginFailMap.get(useruame) != null) {
			loginFailNum = loginFailMap.get(useruame).intValue();
		}
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			if (loginFailNum >= PWD_ERROR_COUNT) {
				Calendar now = Calendar.getInstance();
				now.add(Calendar.MINUTE, PWD_UNLOCK_TIME);
				loginFailMap.put(useruame + ":unlockMillis", now.getTimeInMillis());
			}
			loginFailMap.put(useruame, new Long(loginFailNum));
		}
		if (clean) {
			loginFailMap.remove(useruame);
			loginFailMap.remove(useruame + ":unlockMillis");
		}
		return loginFailNum >= PWD_ERROR_COUNT;
	}

	private String treeHtml() {

		List<Menu> allMenus = UserUtils.getMenuList();
		StringBuffer sbfMenu = new StringBuffer();
		List<Menu> menuList = Lists.newArrayList();
		if (allMenus != null && allMenus.size() > 0) {
			for (Menu menu : allMenus) {
				if (menu.getMenuType() == 0) {
					menuList.add(menu);
				}
			}
		}
		boolean firstMenu = true;
		if (menuList != null && menuList.size() > 0) {
			for (Menu parentMenu : menuList) {
				if ("1".equals(parentMenu.getIsShow())) {
					if ("1".equals(parentMenu.getParentId())) {
						// 一级菜单
						for (Menu menu2 : menuList) {
							if ("1".equals(menu2.getIsShow()) && parentMenu.getId().equals(menu2.getParentId())) {
								if (firstMenu) {
									sbfMenu.append(" <li class=\"nav-first-level\" data-parent=\"");
								} else {
									sbfMenu.append(
											" <li  class=\"nav-first-level\" style=\"display:none\" data-parent=\"");
								}
								sbfMenu.append(menu2.getParentId());
								sbfMenu.append("\">\n");
								sbfMenu.append("  <a parent-parent-name=\"");
								sbfMenu.append(parentMenu.getName());
								sbfMenu.append("\"");
								sbfMenu.append(" href=\"");
								if (StringUtils.isNotBlank(menu2.getHref())) {
									sbfMenu.append(menu2.getHref());
								}

								sbfMenu.append("\">\n");
								sbfMenu.append("<i class=\"fa ");
								if (menu2.getIcon() != null && menu2.getIcon().indexOf(CUSTOMER_ICON_FLAG) >= 0) {
									sbfMenu.append(menu2.getIcon() == null ? "" : menu2.getIcon());
								} else {
									sbfMenu.append(menu2.getIcon() == null ? "" : "fa-" + menu2.getIcon());
								}

								sbfMenu.append("\"></i>\n ");
								sbfMenu.append("<span class=\"nav-label\">");
								sbfMenu.append(menu2.getName());
								sbfMenu.append("</span><span class=\"fa arrow\"></span>");
								sbfMenu.append("</a>\n");
								// 三级菜单
								sbfMenu.append(
										"<ul class=\"nav nav-second-level collapse\" aria-expanded=\"false\" style=\"height: 0px;\"> \n");
								for (Menu menu3 : menuList) {
									if ("1".equals(menu3.getIsShow()) && menu2.getId().equals(menu3.getParentId())) {
										sbfMenu.append("<li>\n");
										sbfMenu.append("  <a class=\"J_menuItem\"  href=\"");
										if (StringUtils.isNotBlank(menu3.getHref())) {
											sbfMenu.append(menu3.getHref());
										} else {
											sbfMenu.append("/404");
										}
										sbfMenu.append("\"");
										sbfMenu.append(">\n");

										sbfMenu.append(menu3.getName());
										sbfMenu.append("</a>\n");
										sbfMenu.append("</li>\n");
									}
								}
								sbfMenu.append("</ul>\n");
								sbfMenu.append(" </li>\n");
							}
						}
						firstMenu = false;
					}
				}
			}
		}

		return sbfMenu.toString();
	}
}
