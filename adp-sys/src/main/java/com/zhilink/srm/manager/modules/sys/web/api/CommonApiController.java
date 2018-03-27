/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web.api;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.Messages;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.basemodel.PageApiResult;
import com.zhilink.manager.framework.common.comstans.Constants;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.CookieUtils;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.exception.CommonException;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.entity.News;
import com.zhilink.srm.manager.modules.sys.entity.ReqLogin;
import com.zhilink.srm.manager.modules.sys.entity.SysExtendWord;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.security.LoginType;
import com.zhilink.srm.manager.modules.sys.security.SystemAuthorizingRealm;
import com.zhilink.srm.manager.modules.sys.security.UsernamePasswordToken;
import com.zhilink.srm.manager.modules.sys.service.AreaService;
import com.zhilink.srm.manager.modules.sys.service.CacheVersionService;
import com.zhilink.srm.manager.modules.sys.service.DictService;
import com.zhilink.srm.manager.modules.sys.service.NewsService;
import com.zhilink.srm.manager.modules.sys.service.SysExtendWordService;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

import feign.Headers;

/**
 * 登录Controller
 * 
 * @author jaray
 * 
 */
@Controller
public class CommonApiController extends BaseController {
	@Autowired
	private SystemAuthorizingRealm sysAuthorizingRealm;

	@Autowired
	private CacheVersionService cacheVersionService;

	@Autowired
	private DictService dictService;

	@Autowired
	private AreaService areaService;
	@Autowired
	private NewsService newsService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SysExtendWordService sysExtendWordService;
	
	// @Autowired
	// AppVersionService appVersionService;
	/**
	 * api 登录
	 */
	@RequestMapping(value = "${apiPath}/loginout")
	@ResponseBody
	public ResultModel apiLoginOut() {
		UserUtils.clearCache();
		UserUtils.getSubject().logout();
		ResultModel result = new ResultModel();
		return result;
	}

	/**
	 * api 登录
	 */
	@RequestMapping(value = "${apiPath}/login")
	@ResponseBody
	public ResultModel login(String loginName, String password, HttpServletResponse response, HttpServletRequest request) {
		ResultModel result = new ResultModel();
		String selectedLanguage = request.getParameter(I18n.LANGKEY);
		Locale locale = null;
		if (StringUtils.isNotBlank(selectedLanguage)) {
			selectedLanguage = selectedLanguage.replace("-", "_");// java中区域国家使用_隔开
			locale = org.springframework.util.StringUtils.parseLocaleString(selectedLanguage);
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}			
		// 缓存到session
		UserUtils.getSession().setAttribute(I18n.LANGKEY, locale);
		
		// 校验验证码 直接pass
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
			result.setErrorCode("-1");
			result.setErrorMsg(I18n.i18nMessage("user.loginError"));
			return result;
		}

		Subject currentUser = UserUtils.getSubject();
		//每次登录执行完整的登录过程，不管是否已经登录过
		/*if (!currentUser.isAuthenticated()) {*/
			UsernamePasswordToken token = new UsernamePasswordToken(loginName, password, LoginType.API);
			try {
				currentUser.login(token);
			} catch (Exception ex) {// login failure
				throw new CommonException(Messages.LOGIN_EXCEPTION_CODE, I18n.i18nMessage("user.loginError"));
			}
	/*	}*/
		
		User user = UserUtils.getUser();
		// 用户语言
		if (user != null) {

			//校验机构不存在则给出提示，并退出
			if(user.getCompany() == null || StringUtils.isBlank(user.getCompany().getCode())){
				UserUtils.clearCache();
				UserUtils.getSubject().logout();
				throw new CommonException(Messages.LOGIN_EXCEPTION_CODE, I18n.i18nMessage("user.noOrganization"));
			}



			if (locale==null&&StringUtils.isNotBlank(user.getLanguage())) {
				locale = org.springframework.util.StringUtils.parseLocaleString(user.getLanguage());
			}

			// 更新用户语言
			user.setLanguage(locale.toString());
			// 保存当前用户的语言
			systemService.updateLangById(user.getId(), user.getLanguage());

			// 输出cookie,记录语言
			CookieUtils.setCookie(response, I18n.LANGKEY, locale.toString());

		}

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("userInfo", user);
		// api 使用token 代替seesion
		body.put(Constants.TOKEN, UserUtils.getSession().getId());
		// app版本信息
		// AppVersion appVersion =appVersionService.getNewAppVersion();
		// body.put("appVersion", appVersion.getVersion());
		// body.put("appUrl", appVersion.getUrl());
		// body.put("appDesc", appVersion.getDescription());
		System.out.println(UserUtils.getSession().getId());
		result.setBody(body);
		return result;
	}
	
	@RequestMapping(value = "${apiPath}/login/api")
	@ResponseBody
	public ResultModel apiLogin(@RequestBody ReqLogin reqLogin, HttpServletResponse response, HttpServletRequest request) {
		return login(reqLogin.getKey(), reqLogin.getSecrect(), response, request);
	}
	
	@RequestMapping(value = "${apiPath}/login/oauth")
	@ResponseBody
	public ResultModel oauthLogin(@RequestBody ReqLogin reqLogin, HttpServletResponse response, HttpServletRequest request) {
		return login(reqLogin.getKey(), reqLogin.getSecrect(), response, request);
	}

	/**
	 * api获取用户菜单权限
	 */
	/* @RequiresPermissions("sys:apimenu:view") */
	@RequestMapping(value = "${apiPath}/getMenuList")
	@ResponseBody
	public ResultModel getMenuList() {
		try {
			ResultModel result = new ResultModel();

			List<Menu> allMenus = UserUtils.getMenuList();
			List<Menu> menuList = Lists.newArrayList();
			if (allMenus != null && allMenus.size() > 0) {
				for (Menu menu : allMenus) {
					if (menu.getMenuType() == 1) {
						menuList.add(menu);
					}
				}
			}

			result.setBody(menuList);
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException();
		}
	}

	/**
	 * 获取当前用户权限列表
	 */
	@RequiresPermissions("sys:apiperm:view")
	@RequestMapping(value = "${apiPath}/userPermissions", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel userPermissions() {
		try {
			ResultModel result = new ResultModel();
			result.setBody(this.sysAuthorizingRealm.getUserPermessions());
			// result.setBody(UserUtils.getCache(key));
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException();
		}
	}

	/**
	 * 当前字典版本
	 */
	@RequestMapping(value = "${apiPath}/curDictCacheVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel curDictCacheVersion() {
		try {
			ResultModel result = new ResultModel();
			result.setBody(this.cacheVersionService.getVersionByName(Constants.DICT_CACHE_VERSION_KEY));
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException();
		}
	}

	/**
	 * 当前字典版本
	 */
	@RequestMapping(value = "${apiPath}/curCacheVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel curCacheVersion() {
		try {
			ResultModel result = new ResultModel();
			result.setBody(this.cacheVersionService.getVersionList());
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException(ex.getMessage());
		}
	}

	/**
	 * 缓存版本
	 */
	@RequestMapping(value = "${apiPath}/getTypeDict", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel getTypeDict(String typeName) {
		try {
			ResultModel result = new ResultModel();
			result.setBody(this.dictService.getTypeDict(typeName));
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException();
		}

	}

	/**
	 * 当前区域版本
	 */
	@RequestMapping(value = "${apiPath}/curAreaCacheVersion", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel curAreaCacheVersion() {
		try {
			ResultModel result = new ResultModel();
			result.setBody(this.cacheVersionService.getVersionByName(Constants.AREA_CACHE_VERSION_KEY));
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException();
		}
	}

	/**
	 * 当前区域版本
	 */

	@RequestMapping(value = "${apiPath}/getArea", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel getAllArea(String parentId) {
		try {
			ResultModel result = new ResultModel();
			result.setBody(this.areaService.findByParentId(parentId));
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException();
		}
	}

	/**
	 * 获取新闻通告列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param subject
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "${apiPath}/getNewsList", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel getNews(Integer pageNumber, Integer pageSize, String subject, String keyword) {
		ResultModel result = new ResultModel();
		if (pageNumber == null) {
			pageNumber = 1;
		}
		if (pageSize == null) {
			pageSize = Integer.valueOf(Global.getConfig("page.pageSize"));
		}
		News news = new News();
		news.getPage().setPageSize(pageSize);
		news.getPage().setPageNo(pageNumber);
		news.getPage().setOrderBy(" create_date desc");
		news.setSubject(subject);
		news.setKeyword(keyword);
		news.setPublish("1");// 显示状态
		news.setTypeId("1");
		String userId = UserUtils.getUser().getId();
		Page<News> page = newsService.findPage(news.getPage(), news);
		PageApiResult<JSONObject> pageDate = new PageApiResult<JSONObject>();
		if (page.getList() != null) {
			List<News> findList = page.getList();
			pageDate.setTotolCount((int) page.getCount());
			List<JSONObject> newsArray = new ArrayList<JSONObject>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (News n : findList) {
				JSONObject jsonOb = new JSONObject();
				jsonOb.put("id", n.getId());
				if (n.getCreateDate() != null) {
					jsonOb.put("createDate", sdf.format(n.getCreateDate()));
				}
				if(userId.equals(n.getAccepterId())){
					jsonOb.put("delStatus", "1");
				}else
				{
					jsonOb.put("delStatus", "0");
				}
				jsonOb.put("subject", n.getSubject());
				newsArray.add(jsonOb);
			}
			pageDate.setListdata(newsArray);
		}
		result.setBody(pageDate);
		return result;
	}

	/**
	 * 新闻详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "${apiPath}/getNewsInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel getNewsInfo(String id) {
		ResultModel result = new ResultModel();
		if (StringUtils.isBlank(id)) {
			result.setErrorMsg(I18n.i18nMessage("api.appAuditParamMiss"));
			return result;
		}
		News news = newsService.get(id);
		String str = news.getContent();
		String  string = str.replaceAll("\\<.*?\\>","");  
    	String content = StringEscapeUtils.unescapeHtml(string);//转义  
    	String  strings = content.replaceAll("\\<.*?\\>","");  
    	String contents = StringEscapeUtils.unescapeHtml(strings);//去掉标签
    	news.setContent(contents);
		// 附件对象
		List<HashMap<String, String>> attachments = null;
		if (news != null && !StringUtils.isBlank(news.getAttachmentId())) {
			String[] AttaPaths = news.getAttachmentId().split("\\|");
			String[] AttaNames = news.getAttachmentName().split("\\|");
			attachments = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> Attachment = null;
			String AttaName = null;
			for (int k = 0; k < AttaPaths.length; k++) {
				if (AttaPaths[k].length() > 0) {
					Attachment = new HashMap<String, String>();
					try {
						AttaName =java.net.URLDecoder.decode(AttaNames[k],"utf-8");
					} catch (UnsupportedEncodingException e) {
						result.setErrorMsg(I18n.i18nMessage("编码转换错误！"));
						e.printStackTrace();
					}
					Attachment.put("name", AttaName);
					Attachment.put("url", AttaPaths[k]);
					attachments.add(Attachment);
				}
			}
		}
		news.setAttachments(attachments);
		result.setBody(news);
		return result;
	}
	
	// 获取当前用户的公告数量

		@RequestMapping(value = "${apiPath}/notifyNum",method = RequestMethod.GET)
		public @ResponseBody ResultModel notifyNum(HttpServletResponse response, HttpServletRequest request) {
			ResultModel resultModel = new ResultModel();
			String userId = UserUtils.getUser().getId();
			String publish = "1";//公告已发布
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("publish", publish);
			map.put("userId", userId);
			int num = newsService.notifyNum(map);
			Map<String, Integer> simplyMap = new HashMap<String, Integer>();
			simplyMap.put("num", num);
			resultModel.setBody(simplyMap);
			return resultModel;
		}
		
		// 点击查看消息时，更新消息状态
		@RequestMapping(value = "${apiPath}/updateStatus",method = RequestMethod.GET)
		public @ResponseBody ResultModel updateStatus(String id) {
			ResultModel resultModel = new ResultModel();
			News news = new News();
			if (StringUtils.isNotBlank(id)) {
				news.setId(id);
			}
			news.setDelStatus("1");// 已读
			news.setAccepterId(UserUtils.getUser().getId());
			try {
				News news2 = newsService.get(id);
				news.setClickCount(news2.getClickCount() + 1);//点击数
				news.setUpdateBy(news2.getUpdateBy());
				news.setUpdateDate(news2.getUpdateDate());
				newsService.updateStatus(news);//更新 消息状态
			} catch (Exception e) {
				resultModel.setErrorMsg("查看消息，更新失败");
				return resultModel;
			}
			return resultModel;

		}
	/**
	 * 国际化语言切换
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "${apiPath}/changeLanguage")
	public @ResponseBody ResultModel changeLanguage(HttpServletRequest request, HttpServletResponse response) {

		ResultModel result = new ResultModel();
		String newLocale = request.getParameter(I18n.LANGKEY);
		Locale curLocale = org.springframework.util.StringUtils.parseLocaleString(newLocale);
		if (curLocale != null) {
			User user = UserUtils.getUser();
			if (user != null) {
				// 更新缓存
				user.setLanguage(curLocale.toString());
				// 保存当前用户的语言
				systemService.updateLangById(user.getId(), user.getLanguage());
				// 缓存到session
				UserUtils.getSession().setAttribute(I18n.LANGKEY, curLocale);
			}
			// 输出cookie,记录语言
			CookieUtils.setCookie(response, I18n.LANGKEY, curLocale.toString());

		} else {
			result.setErrorMsg(I18n.i18nMessage("api.switchLangageException"));
		}
		return result;
	}
	
	/**
	 * 扩展字段
	 * @param key
	 */
	@RequestMapping(value = "${apiPath}/open/extendWordList",  method = RequestMethod.GET)
	@ResponseBody
	public ResultModel extendWordList() {
		
		ResultModel resultModel = new ResultModel();
		List<SysExtendWord> extendWordList = sysExtendWordService.findList(new SysExtendWord());
		
		List<String> keys = new ArrayList<String>();
		
		if(extendWordList.size() > 0){
			Map<String,List<JSONObject>> maps = new HashMap<String,List<JSONObject>>();
			for(SysExtendWord e : extendWordList){
				if(!keys.contains(e.getKey())){
					List<JSONObject> list = new ArrayList<JSONObject>();
					keys.add(e.getKey());
					JSONObject jsobj = new JSONObject();
					jsobj.put("name", e.getName());
					jsobj.put("displayName", e.getDisplayName());
					jsobj.put("type", e.getType());
					jsobj.put("displayNameCN", e.getDisplayNameCN());
					jsobj.put("displayNameTW", e.getDisplayNameTW());
					jsobj.put("displayNameUN", e.getDisplayNameUN());
					list.add(jsobj);
					maps.put(e.getKey(), list);
				}else{
					JSONObject jsobj = new JSONObject();
					jsobj.put("name", e.getName());
					jsobj.put("displayName", e.getDisplayName());
					jsobj.put("type", e.getType());
					jsobj.put("displayNameCN", e.getDisplayNameCN());
					jsobj.put("displayNameTW", e.getDisplayNameTW());
					jsobj.put("displayNameUN", e.getDisplayNameUN());
					maps.get(e.getKey()).add(jsobj);
				}
			}
			List<JSONObject> jsonList = new ArrayList<JSONObject>();
			for(String key:maps.keySet()){
				JSONObject js = new JSONObject();
				js.put("key", key);
				js.put("items", maps.get(key));
				jsonList.add(js);
			}
			resultModel.setBody(jsonList);
		}
		return resultModel;
	}

}
