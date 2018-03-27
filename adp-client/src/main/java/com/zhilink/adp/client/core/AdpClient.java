package com.zhilink.adp.client.core;

import java.util.concurrent.ConcurrentHashMap;

import com.zhilink.adp.client.api.BaseInterface;
import com.zhilink.adp.client.codec.ApiGsonDecoder;
import com.zhilink.adp.client.codec.ApiGsonEncoder;
import com.zhilink.adp.client.intercepter.TokenRequestInterceptor;
import com.zhilink.adp.client.model.Token;
import feign.Feign;

public class AdpClient {
	public static final short LOGIN = 1;
	public static final short OAUTH = 2;
	private BaseInterface base = null;
	private ConcurrentHashMap<String, Object> apiCache = new ConcurrentHashMap<String, Object>();
	private String token;
	private OkHttpClient okHttpClient;
	/**
	 * 类型 1-用户密码, 2-appserect
	 */
	private short type = 1;

	private String serverApiUrl = null;

	private String key;

	private String secrect;

	public String getServerApiUrl() {
		return this.serverApiUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isAuth() {
		if (null != this.token) {
			return true;
		} else {
			return false;
		}
	}

	public void setServerApiUrl(String serverApiUrl) {
		this.serverApiUrl = serverApiUrl;
	}

	public AdpClient(String serverApiUrl, String key, String secrect, short type) {
		this.type = type;
		this.key = key;
		this.secrect = secrect;
		this.serverApiUrl = serverApiUrl;
		base = Feign.builder().decoder(new ApiGsonDecoder(this))
				// .encoder(new GsonEncoder())
				.client(getOkHttpClient()).target(BaseInterface.class, this.serverApiUrl);
	}

	public AdpClient(String serverApiUrl, String key, String secrect) {
		this(serverApiUrl, key, secrect, LOGIN);
	}

	private synchronized OkHttpClient getOkHttpClient() {
		if (this.okHttpClient == null) {
			this.okHttpClient = new OkHttpClient(this);
		}
		return okHttpClient;
	}

	/**
	 * 刷新token
	 */
	public synchronized void refreshToken() {
		if (this.type == 1) {
			Token token = base.getTokenByLogin(this.key, this.secrect);
			this.token = token.getToken();
			// this.getOkHttpClient().addHeaderToken(token.getToken());
			System.out.println("获取token=" + token.getToken());
		}
	}

	/**
	 * 使token失效
	 * 
	 * @param Type
	 * @return
	 */
	public void InvalidToken() {
		this.token = null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> Type) {
		T obj = null;
		try {
			obj = (T) apiCache.get(Type.getName());
			if (obj == null) {
				obj = (T) Feign.builder().decoder(new ApiGsonDecoder(this)).encoder(new ApiGsonEncoder(this))
						.requestInterceptor(new TokenRequestInterceptor(this)).client(getOkHttpClient())
						.target(Type, this.serverApiUrl);
				this.apiCache.put(Type.getName(), obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> Type,String url) {
		T obj = null;
		try {
			obj = (T) apiCache.get(Type.getName());
			if (obj == null) {
				obj = (T) Feign.builder().decoder(new ApiGsonDecoder(this)).encoder(new ApiGsonEncoder(this))
						.requestInterceptor(new TokenRequestInterceptor(this)).client(getOkHttpClient())
						.target(Type, url);
				this.apiCache.put(Type.getName(), obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getNoAuthObject(Class<T> Type) {
		T obj = null;
		try {
			obj = (T) apiCache.get(Type.getName());
			if (obj == null) {
				obj = (T) Feign.builder().decoder(new ApiGsonDecoder(this)).encoder(new ApiGsonEncoder(this))
						.client(getOkHttpClient()).target(Type, this.serverApiUrl);
				this.apiCache.put(Type.getName(), obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
