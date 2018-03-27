package com.zhilink.srm.manager.modules.sys.web.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.common.token.Token;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.comstans.Constants;
import com.zhilink.manager.framework.common.exception.CommonException;
import com.zhilink.manager.framework.common.utils.CookieUtils;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.AppApply;
import com.zhilink.srm.manager.modules.sys.entity.SysAppList;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.security.LoginType;
import com.zhilink.srm.manager.modules.sys.security.UsernamePasswordToken;
import com.zhilink.srm.manager.modules.sys.service.AppApplyService;
import com.zhilink.srm.manager.modules.sys.service.AppAuthService;
import com.zhilink.srm.manager.modules.sys.service.SysAppListService;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * app授权
 * @author benxu
 */
@Controller
@RequestMapping(value = "${apiPath}/sys/app")
public class AppAuthApiController extends BaseController {
		
	@Autowired
	private AppAuthService appAuthService;
		
	@Autowired
	private AppApplyService appApplyService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private SysAppListService sysAppListService;

	
	/**
	 * 获取授权信息
	 * @param appid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/authinfo")
	@ResponseBody
	public ResultModel getAuthInfo(String appid, String code) {
		ResultModel result = new ResultModel();
		JSONObject objApplyInfo=new JSONObject();
		logger.info("==================appid:"+appid+",code:"+code+"====================");
		if(StringUtils.isBlank(appid) || StringUtils.isBlank(code)){
			throw new CommonException(I18n.i18nMessage("api.appAuditParamMiss"));
		}
		try {				
			String openid = appAuthService.getOpenId(appid, code);
			objApplyInfo.put("openid", openid);
			
			AppApply apply=appApplyService.get(openid);
			if(apply==null || "1".equals(apply.getDelFlag())){
				objApplyInfo.put("auth", -1);
				objApplyInfo.put("authMsg", I18n.i18nMessage("api.noAuditionApplication"));
				result.setStatus(0);
			}else if("0".equals(apply.getStatus())){
				objApplyInfo.put("auth", 0);
				objApplyInfo.put("authMsg", I18n.i18nMessage("api.authorizeWaitAudit"));
				result.setStatus(0);
			}else if("1".equals(apply.getStatus())){
				objApplyInfo= JSONObject.parseObject(apply.getApplyInfo());
				objApplyInfo.put("auth", 1);
				objApplyInfo.put("authMsg", "");
				if(!StringUtils.isBlank(apply.getRelateUser())){
					User user= systemService.getUser(apply.getRelateUser());
					if(user !=null){
						objApplyInfo.put("user", user);
					}
				}
				result.setStatus(0);
			}else if("2".equals(apply.getStatus())){
				objApplyInfo.put("auth", 2);
				objApplyInfo.put("authMsg", apply.getRemarks());
				result.setStatus(0);
			}else{
				throw new CommonException(I18n.i18nMessage("api.unknownStatus"));
			}
					
			System.out.println(objApplyInfo.toJSONString());
			result.setBody(objApplyInfo);
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof CommonException){
				throw e;
			}else{
				throw new CommonException(I18n.i18nMessage("api.unknownError"));
			}
		}
		logger.info("appid:{} code:{},result:{}",appid,code,JSON.toJSONString(result));
		return result;
	}
	
	/**
	 * 申请授权信息
	 * @param headers
	 * @param bodys
	 * @return
	 */
	@RequestMapping(value = "/authapply", method = RequestMethod.POST)
	@ResponseBody
	public ResultModel authApply(@RequestHeader HttpHeaders headers,@RequestBody String bodys) {
		ResultModel result = new ResultModel();
		logger.info("authApply>>bodys:{}",bodys);
		try {
			JSONObject objBody = JSONObject.parseObject(bodys);
			String appid=objBody.getString("appid");
			String openid=objBody.getString("openid");
			if(!objBody.containsKey("appid") || !objBody.containsKey("openid") || StringUtils.isBlank(appid) || StringUtils.isBlank(openid)){
				throw new CommonException(I18n.i18nMessage("api.appAuditParamMiss"));
			}
			//String code=objBody.getString("code");
			//String openId = appAuthService.getOpenId(appid, code);
			AppApply apply=appApplyService.get(openid);
			if(apply !=null && "0".equals(apply.getDelFlag())){
				if("0".equals( apply.getStatus())){
					throw new CommonException(I18n.i18nMessage("api.authorizeWaitAudit"));
				}else if("1".equals( apply.getStatus())){
					throw new CommonException(I18n.i18nMessage("api.auditted"));
				}			
			}		
			//保存到申请表，如果存在则覆盖,已审核则提示错误
			boolean isNew=false;
			if(apply==null){
				isNew=true;
				apply=new AppApply();
				apply.setOpenId(openid);
				apply.setAppId(appid);
				apply.setCreateDate(new Date());
				apply.setCreateBy(UserUtils.getUser());
				//apply.setUpdateDate(new Date());
				//apply.setUpdateBy(UserUtils.getUser());
			}
			objBody.remove("appid");
			objBody.remove("openid");
			objBody.remove("code");
			apply.setApplyInfo(objBody.toJSONString());
			apply.setStatus("0");
			apply.setRemarks("");
			apply.setDelFlag("0");
			if(isNew){
				appApplyService.insertApply(apply);
			}else{
				apply.setUpdateDate(new Date());
				apply.setUpdateBy(UserUtils.getUser());
				appApplyService.updateApply(apply);
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			if(e instanceof CommonException){
				throw e;
			}else{
				throw new CommonException(I18n.i18nMessage("api.unknownError"));
			}
		}	
		logger.info("authApply>>bodys:{}, result:{}",bodys, JSON.toJSONString(result));
		return result;
	}

	/**
	 * 获取授权
	 * @param appid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	@ResponseBody
	public ResultModel auth(String appid, String code,HttpServletResponse response, HttpServletRequest request) {
		ResultModel result = new ResultModel();
		logger.info("==================appid:"+appid+",code:"+code+"====================");
		if(StringUtils.isBlank(appid)){
			throw new CommonException(I18n.i18nMessage("api.appAuditParamMiss"));
		}
		
		//获取app信息
		SysAppList appInfo= sysAppListService.getAppInfo(appid);
		if(appInfo==null){
			throw new CommonException(I18n.i18nMessage("api.unknown")+"app！");
		}
		if(StringUtils.isEmpty(code)){
			throw new CommonException(I18n.i18nMessage("api.appAuditParamMiss"));
		}
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
		
		/*String tokenUrl="http://localhost:8080/bac-wportal/auth2/access_token?appid=%s&secret=%s&code=%s";
		tokenUrl=String.format(tokenUrl, appid,secret,code);
		String strAccessToken=HttpUtil.sendGet(tokenUrl, "utf-8");
		JSONObject objAccessToken=JSONObject.parseObject(strAccessToken);
		String accessToken=objAccessToken.getJSONObject("body").getString("access_token");
		String openid=objAccessToken.getJSONObject("body").getString("openid");
		if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(openid)){
			return "redirect:" + authUrl;
		}*/
		Map<String, String> mapAccessToken = appAuthService.getAccessToken(appInfo, code);
		String openid=mapAccessToken.get("openid").toString();
		//String accessToken=mapAccessToken.get("access_token").toString();
		
		AppApply apply=appApplyService.get(openid);
		if(apply==null || "1".equals(apply.getDelFlag())){
			//throw new CommonException("-1", "未获取授权申请");
			/*String userinfoUrl="http://localhost:8080/bac-wportal/open/userinfo?accessToken=%s&openid=%s";
			userinfoUrl=String.format(userinfoUrl, accessToken,openid);
			String strUserInfo=HttpUtil.sendGet(userinfoUrl, "utf-8");
			JSONObject objUserInfo=JSONObject.parseObject(strUserInfo);
			if(objUserInfo.getJSONObject("head").getInteger("status")==0){
				
			}*/
			JSONObject objUserInfo=appAuthService.getAuthUserInfo(appInfo, mapAccessToken);
			
			throw new CommonException(I18n.i18nMessage("api.noAuditionApplication"));
		}else if("0".equals(apply.getStatus())){
			throw new CommonException("-2", I18n.i18nMessage("api.authorizeWaitAudit"));
		}else if("1".equals(apply.getStatus())){
			User user= systemService.getUser(apply.getRelateUser());
			if(user ==null){
				throw new CommonException("-4", I18n.i18nMessage("api.lackOfRelationshipUserInfomation"));
			}
			//自动登录跳到首页
			UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getLoginName(), LoginType.OTHRE);			
			Subject currentSub = UserUtils.getSubject();
			currentSub.login(token);
			User currentUser = UserUtils.getUser();
			// 用户语言
			if (currentUser != null) {
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
		}else if("2".equals(apply.getStatus())){
			throw new CommonException("-3", I18n.i18nMessage("api.refusedAuthorize")+apply.getRemarks());
		}else{
			throw new CommonException(I18n.i18nMessage("api.unknownStatus"));
		}
		
	}
	
	/**
	 *  自动登录
	 * @param appid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/autologin", method = RequestMethod.GET)
	public String autologin(HttpServletRequest request, HttpServletResponse response) {
		String loginName= request.getParameter("loginName");
		User user= systemService.getUserByLoginName(loginName);
		if(user==null){
			return "redirect:modules/sys/sysLogin";
		}
		
		UsernamePasswordToken token = new UsernamePasswordToken(loginName, loginName, LoginType.OTHRE);
		
		Subject currentUser = UserUtils.getSubject();
		
		currentUser.login(token);
		
		return "redirect:" + adminPath ;
	}

	
	/**
	 *  关联用户
	 * @param appid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/relateUser")
	@ResponseBody
	public ResultModel relateUser(HttpServletRequest request, HttpServletResponse response,String appid,String code) {
		ResultModel result = new ResultModel();
		User user = UserUtils.getUser();
		String userId = user.getId();
		if(StringUtils.isBlank(appid)){
			throw new CommonException(I18n.i18nMessage("api.appAuditParamMiss"));
		}
		if(StringUtils.isBlank(code)){
			throw new CommonException(I18n.i18nMessage("api.appAuditParamMiss"));
		}
		if(StringUtils.isBlank(userId)){
			throw new CommonException(I18n.i18nMessage("api.notExsitThisUser"));
		}
		SysAppList appInfo= sysAppListService.getAppInfo(appid);
		if(appInfo==null){
			throw new CommonException(I18n.i18nMessage("api.unknown")+"app！");
		}
		Map<String, String> mapAccessToken = appAuthService.getAccessToken(appInfo, code);
		String openid=mapAccessToken.get("openid");
		if(openid==null){
			throw new CommonException("app"+I18n.i18nMessage("api.appAuditParamMiss"));
		}
//		User user = systemService.getUser(userId);
		if(StringUtils.isBlank(userId)){
			throw new CommonException(I18n.i18nMessage("api.notExsitThisUser"));
		}
		JSONObject authUserInfo = appAuthService.getAuthUserInfo(appInfo, mapAccessToken);
		AppApply oldAppApply = appApplyService.get(openid);
		if(oldAppApply!=null){
			//暂时不做重复校验验证,
//			appApplyService.deleteByPhy(openid);
			if(!StringUtils.isEmpty(oldAppApply.getRelateUser())){
				throw new CommonException(I18n.i18nMessage("api.bacExistBund"));
			}else{
				oldAppApply.setRelateUser(userId);
				oldAppApply.setApplyInfo(authUserInfo.toJSONString());
				appApplyService.updateApply(oldAppApply);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("result", true);
				map.put("userInfo", authUserInfo);
				result.setBody(map);
				return result;
			}
		}
		if(appApplyService.selectByRelateUser(user.getId())!=null){
			throw new CommonException(I18n.i18nMessage("api.userExistBund"));
		}
		//获取app信息
		if(!appAuthService.bindToBac(authUserInfo.getString("uid"), appid)){
			throw new CommonException(I18n.i18nMessage("api.notExsitThisUser"));
		};
		AppApply appApply = new AppApply();
		appApply.setAppId(appid);
		appApply.setOpenId(openid);
		appApply.setApplyInfo(authUserInfo.toJSONString());
		appApply.setRelateUser(userId);
		appApply.setStatus("1");
		appApply.setRemarks("bac"+I18n.i18nMessage("api.userBound"));
		appApplyService.save(appApply);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("result", true);
		map.put("userInfo", authUserInfo);
		result.setBody(map);
		return result;
	}
	
	@RequestMapping(value = "/isRelateUser")
	@ResponseBody
	public ResultModel isRelateUser(){
		ResultModel result = new ResultModel();
		User user = UserUtils.getUser();
		String userId = user.getId();
		if(StringUtils.isBlank(userId)){
			throw new CommonException(I18n.i18nMessage("api.notExsitThisUser"));
		}
		Map<String,Object> map = new HashMap<String, Object>();
		AppApply appApply = appApplyService.selectByRelateUser(user.getId());
		if(appApply==null){
			map.put("result", false);
		}else{
			map.put("result", true);
			map.put("userInfo", JSON.parse(appApply.getApplyInfo()));
		}
		result.setBody(map);
		return result;
	}
	
	@RequestMapping(value = "/getAppid")
	@ResponseBody
	public ResultModel getAppid(){
		ResultModel result = new ResultModel();
		Map<String,Object> map = new HashMap<String, Object>();
		SysAppList findapp = new SysAppList();
		findapp.setAppType("4");
		List<SysAppList> sysAppList = sysAppListService.findList(findapp);
		if(sysAppList==null||sysAppList.size()==0){
			throw new CommonException(I18n.i18nMessage("api.platformNotRegister"));
		}else{
			map.put("appid", sysAppList.get(0).getAppId());
		}
		result.setBody(map);
		return result;
	}
//	public AppApply checkRelateUser(User user){
//		AppApply appApply = appApplyService.selectByRelateUser(user.getId());
//		if(appApply==null){
//			return null;
//		}
//		return appApply;
//	}
	
	/**
	 *  关联用户
	 * @param appid
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/unbund")
	@ResponseBody
	public ResultModel unbund(HttpServletRequest request, HttpServletResponse response,String code,String appid) {
		ResultModel result = new ResultModel();
		User user = UserUtils.getUser();
		if(user ==null){
			throw new CommonException(I18n.i18nMessage("api.notExsitThisUser"));
		}
//		//如果appid和code不为空，则代表可能是bac发起的解绑行为
//		if(appid!=null&&code!=null){
//			SysAppList appInfo= sysAppListService.getAppInfo(appid);
//			if(appInfo==null){
//				throw new CommonException(I18n.i18nMessage("api.unknown")+"app！");
//			}
//			Map<String, String> mapAccessToken = appAuthService.getAccessToken(appInfo, code);
//			String openid=mapAccessToken.get("openid");
//			AppApply oldAppApply = appApplyService.get(openid);
//			if(oldAppApply==null){
//				throw new CommonException(I18n.i18nMessage("api.bacUnexistBund"));
//			}
//			appApplyService.deleteByPhy(openid);
//			Map<String,Object> map = new HashMap<String, Object>();
//			map.put("result", true);
//			//map.put("userInfo", authUserInfo。toString());
//			result.setBody(map);
//			return result;
//		}
		AppApply appApply = appApplyService.selectByRelateUser(user.getId());
		if(appApply==null){
			throw new CommonException(I18n.i18nMessage("api.userUnexistBund"));
		}
		if(!appAuthService.unbindToBac(JSONObject.parseObject((appApply.getApplyInfo())).getString("uid"), appid)){
			throw new CommonException(I18n.i18nMessage("api.notExsitThisUser"));
		};
		appApplyService.deleteByPhy(appApply.getOpenId());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("result", true);
		map.put("userInfo", JSON.parse(appApply.getApplyInfo()));
		result.setBody(map);
		return result;
	}
}
