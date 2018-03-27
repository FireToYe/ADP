/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.security;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户和密码（包含验证码）令牌类
 * @author jaray
 * 
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;
	private boolean mobileLogin;
	private int loginType;//0 pc 1 srm 2 wms
	private Map<String,Object> dataMap=new HashMap<String,Object>();
	public UsernamePasswordToken() {
		super();
	}

	public UsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha, boolean mobileLogin) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
		this.mobileLogin = mobileLogin;
	}
	public UsernamePasswordToken(String username, String password, int loginType ) {
		super(username, password );
		this.loginType = loginType;
	}
	
	
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public boolean isMobileLogin() {
		return mobileLogin;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}
	
	
	public void put(String key,Object o){
		this.dataMap.put(key, o);
	}
	
	public Object get(String key){
		return this.dataMap.get(key);
	}
	
	
	
	
}