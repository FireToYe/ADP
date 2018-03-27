package com.zhilink.manager.common.token;

import java.io.Serializable;

import com.zhilink.manager.framework.common.utils.MD5Tools;

/**
 * token
 * 
 * @author jaray
 *
 */
public class Token implements Serializable {

	private static final long serialVersionUID = 699563067947562643L;
	private String token;// token 值
	private long timestamp = System.currentTimeMillis();
	private String id; // 用户Id
	private String loginName;// 用户姓名
	private String sign;// 签名 单点登录中心 内部服务器 存储进行绑定

	public Token(String id, String loginName, String sign) {
		super();
		this.id = id;
		this.loginName = loginName;
		this.sign = sign;
		this.token = MD5Tools.MD5(this.id + this.timestamp + this.sign);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}

	public String getloginName() {
		return loginName;
	}

	public void setloginName(String loginName) {
		this.loginName = loginName;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "Token [token=" + token + ", timestamp=" + timestamp + ", id=" + id + ", loginName=" + loginName
				+ ", sign=" + sign + "]";
	}

}
