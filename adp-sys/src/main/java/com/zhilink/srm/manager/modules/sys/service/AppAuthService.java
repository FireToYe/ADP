package com.zhilink.srm.manager.modules.sys.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.belerweb.social.bean.Result;
import com.belerweb.social.qq.connect.api.QQConnect;
import com.belerweb.social.qq.connect.bean.OpenID;
import com.belerweb.social.weibo.api.Weibo;
import com.belerweb.social.weibo.bean.TokenInfo;
import com.belerweb.social.weixin.api.Weixin;
import com.zhilink.manager.common.utils.Encodes;
import com.zhilink.manager.common.utils.HttpUtil;
import com.zhilink.manager.framework.common.exception.CommonException;
import com.zhilink.srm.common.service.BaseService;
import com.zhilink.srm.manager.modules.sys.entity.SysAppList;

@Service
@Transactional(readOnly = true)
public class AppAuthService  extends BaseService{
	
	@Autowired
	private SysAppListService sysAppListService;
	
	@Value("${domainBac:http://172.31.75.176:8888}")
	private String domainBac;
	
	/**
	 * 获取AccessToken 微信
	 * @param code
	 * @param appInfo
	 * @return
	 */
	public Map<String, String> getAccessTokenWeixin(String code, SysAppList appInfo){
		Map<String, String> accessToken=new HashMap<String, String>();
		
		String appId=appInfo.getAppId();
		String appSecret=appInfo.getAppSecret();
		String token=appInfo.getToken();
		String redirect=appInfo.getRedirect();
		
		//通过参数设置对象
		Weixin weixin = new Weixin(appId, appSecret, redirect, token);
		
		//通过授权码code 获取accessToken
		Result<com.belerweb.social.weixin.bean.AccessToken> tokenResult = weixin.getOAuth2().accessToken(code);
		if(tokenResult.getError() != null){
			throw new CommonException(tokenResult.getError().getErrorCode(),tokenResult.getError().getError());
			
		}
		
		accessToken.put("accessToken", tokenResult.getResult().getToken());
		accessToken.put("openId", tokenResult.getResult().getOpenId());
		return accessToken;
	}
	
	/**
	 * 获取AccessToken QQ
	 * @param code
	 * @param appInfo
	 * @return
	 */
	public Map<String, String> getAccessTokenQQ(String code, SysAppList appInfo){
		Map<String, String> accessToken=new HashMap<String, String>();
		
		String appId=appInfo.getAppId();
		String appSecret=appInfo.getAppSecret();
		String redirect=appInfo.getRedirect();
		
		//设置QQ连接的参数
		QQConnect connect = new QQConnect(appId, appSecret, redirect);
		
		//通过Authorization Code获取Access Token
		Result<com.belerweb.social.qq.connect.bean.AccessToken> tokenResult = connect.getOAuth2().accessToken(code);
		if(tokenResult.getError() != null){
			throw new CommonException(tokenResult.getError().getErrorCode(),tokenResult.getError().getError());
			
		}
		accessToken.put("accessToken", tokenResult.getResult().getToken());
		
		//获取用户openid
		Result<OpenID> openIdResult = connect.getOAuth2().openId(tokenResult.getResult().getToken());
		if(openIdResult.getError() != null) {
			throw new CommonException(openIdResult.getError().getErrorCode(),openIdResult.getError().getError());
		}
		accessToken.put("openId", openIdResult.getResult().getOpenId());
		
		return accessToken;
	}
	
	/**
	 * 获取AccessToken 微博
	 * @param code
	 * @param appInfo
	 * @return
	 */
	public Map<String, String> getAccessTokenWeibo(String code, SysAppList appInfo){
		Map<String, String> accessToken=new HashMap<String, String>();
		
		String appId=appInfo.getAppId();
		String appSecret=appInfo.getAppSecret();
		String redirect=appInfo.getRedirect();
		
		//根据参数设置对象
		Weibo weibo = new Weibo(appId, appSecret, redirect);
		
		//通过code获取 accessToken
		Result<com.belerweb.social.weibo.bean.AccessToken> tokenResult = weibo.getOAuth2().accessToken(code);
		if(tokenResult.getError() != null){
			throw new CommonException(tokenResult.getError().getErrorCode(),tokenResult.getError().getError());
		}
		String _token=tokenResult.getResult().getToken();
		accessToken.put("accessToken", _token);
		
		//通过accessToken 获取openId
		Result<TokenInfo> tokenInfo = weibo.getOAuth2().getTokenInfo(_token);
		if(tokenInfo.getError() != null){
			throw new CommonException(tokenInfo.getError().getErrorCode(),tokenInfo.getError().getError());
		}
		
		String uid=tokenInfo.getResult().getUid();
		//根据openID获取用户的信息
		Result<com.belerweb.social.weibo.bean.User> resultUser = weibo.getUser().show(appId, _token, uid, null);
		if(resultUser.getError() != null){
			throw new CommonException(resultUser.getError().getErrorCode(),resultUser.getError().getError());
		}
		
		accessToken.put("openId", resultUser.getResult().getId());
		
		return accessToken;
	}
	
	/**
	 * 获取AccessToken 即采资讯
	 * @param code
	 * @param appInfo
	 * @return
	 */
	public Map<String, String> getAccessTokenBac(String code, SysAppList appInfo){
		Map<String, String> accessToken=new HashMap<String, String>();
		
		String appId=appInfo.getAppId();
		String appSecret=appInfo.getAppSecret();
		//String redirect=appInfo.getRedirect();
		
		//String domainBac="http://localhost:8080";
		
		String tokenUrl= String.format(domainBac+"/bac-wportal/auth2/access_token?appid=%s&secret=%s&code=%s", appId,appSecret,code);
		String strAccessToken=HttpUtil.sendGet(tokenUrl, "utf-8");
		JSONObject objAccessToken=JSONObject.parseObject(strAccessToken);
		JSONObject objHead=objAccessToken.getJSONObject("head");
		if(objHead.getInteger("status") !=1){
			throw new CommonException(objHead.getString("errorCode"), objHead.getString("errorMsg"));
		}
		if(objAccessToken.get("body") !=null){
			JSONObject objBody=objAccessToken.getJSONObject("body");
			for(Entry<String, Object> kv : objBody.entrySet()){
				accessToken.put(kv.getKey(), kv.getValue().toString());
			}
		}
		
		return accessToken;
	}

	
	/**
	 * 获取OpenId
	 * @param appid
	 * @param code
	 * @return
	 */
	public String getOpenId(String appid, String code){
		Map<String, String> accessToken=getAccessToken(appid, code);
		return accessToken.containsKey("openid") ? accessToken.get("openid").toString() : null;
	}
	
	/**
	 * 获取accessToken
	 * @param appid
	 * @param code
	 * @return
	 */
	public Map<String, String> getAccessToken(String appid, String code){
		//获取app信息
		SysAppList appInfo= sysAppListService.getAppInfo(appid);
		return getAccessToken(appInfo, code);
	}
	
	/**
	 * 获取accessToken
	 * @param appInfo
	 * @param code
	 * @return
	 */
	public Map<String, String> getAccessToken(SysAppList appInfo, String code){
		Map<String, String> accessToken=new HashMap<String, String>();
		if("1".equals(appInfo.getAppType())){
			accessToken= getAccessTokenWeixin(code, appInfo);
		}else if("2".equals(appInfo.getAppType())){
			accessToken= getAccessTokenQQ(code, appInfo);
		}else if("3".equals(appInfo.getAppType())){
			accessToken= getAccessTokenWeibo(code, appInfo);
		}else if("4".equals(appInfo.getAppType())){
			accessToken= getAccessTokenBac(code, appInfo);
		}else{
			throw new CommonException("未知app类型");
		}		
		return accessToken;		
	}
	
	public boolean bindToBac(String uId,String appId){
		String tokenUrl= domainBac+"/bac-wportal/user/app/bind";
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("uId", uId);
		paramMap.put("appId", appId);
		String strUserInfo=HttpUtil.sendPost(tokenUrl, paramMap, "utf-8");
		JSONObject objUserInfo=JSONObject.parseObject(strUserInfo);
		JSONObject objHead=objUserInfo.getJSONObject("head");
		if(objHead.getInteger("status") !=1){
			throw new CommonException(objHead.getString("errorCode"), objHead.getString("errorMsg"));
		}
		return true;
	}
	
	public boolean unbindToBac(String uId,String appId){
		String tokenUrl= domainBac+"/bac-wportal/user/app/unbind";
		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("uId", uId);
		paramMap.put("appId", appId);
		String strUserInfo=HttpUtil.sendPost(tokenUrl, paramMap, "utf-8");
		JSONObject objUserInfo=JSONObject.parseObject(strUserInfo);
		JSONObject objHead=objUserInfo.getJSONObject("head");
		if(objHead.getInteger("status") !=1){
			throw new CommonException(objHead.getString("errorCode"), objHead.getString("errorMsg"));
		}
		return true;
	}
	/**
	 * 获取第三方授权地址
	 * @param appInfo
	 * @return
	 */
	public String getAppAuthUrl(SysAppList appInfo){
		String appAuthUrl = null;
		
		String appId=appInfo.getAppId();
		//String appSecret=appInfo.getAppSecret();
		String redirect=appInfo.getRedirect();
		if("1".equals(appInfo.getAppType())){
			//appAuthUrl= "";
		}else if("2".equals(appInfo.getAppType())){
			//appAuthUrl= "";
		}else if("3".equals(appInfo.getAppType())){
			//appAuthUrl= "";
		}else if("4".equals(appInfo.getAppType())){
			appAuthUrl= domainBac+"/bac-wportal/auth.jsp?appid="+appId+"&redirect_uri="+Encodes.urlEncode(redirect);
		}else{
			throw new CommonException("未知app类型");
		}
		return appAuthUrl;
	}
	
	/**
	 * 获取第三方用户信息
	 * @param appInfo
	 * @param mapAccessToken
	 * @return
	 */
	public JSONObject getAuthUserInfo(SysAppList appInfo, Map<String, String> mapAccessToken){
		JSONObject objUserInfo=new JSONObject();
		if("1".equals(appInfo.getAppType())){
			//objUserInfo= getUserInfoWeixin(mapAccessToken);
		}else if("2".equals(appInfo.getAppType())){
			//objUserInfo= getUserInfoQQ(mapAccessToken);
		}else if("3".equals(appInfo.getAppType())){
			//objUserInfo= getUserInfoWeibo(mapAccessToken);
		}else if("4".equals(appInfo.getAppType())){
			objUserInfo= getUserInfoBac(mapAccessToken);
		}else{
			throw new CommonException("未知app类型");
		}
		return objUserInfo;
	}
	
	/**
	 * 获取用户信息 即采资讯
	 * @param mapAccessToken
	 * @return
	 */
	public JSONObject getUserInfoBac(Map<String, String> mapAccessToken){
		String openid=mapAccessToken.get("openid").toString();
		String accessToken=mapAccessToken.get("access_token").toString();
				
		String userinfoUrl=String.format(domainBac+"/bac-wportal/open/userinfo?accessToken=%s&openid=%s", accessToken,openid);
		String strUserInfo=HttpUtil.sendGet(userinfoUrl, "utf-8");
		JSONObject objUserInfo=JSONObject.parseObject(strUserInfo);
		JSONObject objHead=objUserInfo.getJSONObject("head");
		if(objHead.getInteger("status") !=1){
			throw new CommonException(objHead.getString("errorCode"), objHead.getString("errorMsg"));
		}
		JSONObject objBody=objUserInfo.getJSONObject("body");
		return objBody;
	}

	
	

}
