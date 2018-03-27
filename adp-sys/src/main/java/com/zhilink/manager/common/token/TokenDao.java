package com.zhilink.manager.common.token;

public interface TokenDao {
	public void addToken(Token token);

	public Token readToken(String tokenKey);

	public void deleteToken(String tokenKey);

	public void refreshTokenByKey(String tokenKey);
}
