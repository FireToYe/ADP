package com.zhilink.manager.common.token;

import java.util.concurrent.ConcurrentHashMap;

public class MemTokenDao implements TokenDao {
	private ConcurrentHashMap<String, Token> store = new ConcurrentHashMap<String, Token>();

	public void addToken(Token token) {
		this.store.put(token.getToken(), token);
	}

	public Token readToken(String tokenKey) {
		return this.store.get(tokenKey);
	}

	public void deleteToken(String tokenKey) {
		this.store.remove(tokenKey);

	}

	public void refreshTokenByKey(String tokenKey) {
  
	}

}
