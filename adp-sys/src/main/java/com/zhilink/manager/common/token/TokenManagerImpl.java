package com.zhilink.manager.common.token;

public class TokenManagerImpl implements TokenManager {
	private TokenDao tokenDao;

	public Token getToken(String token) {
		return this.tokenDao.readToken(token);
	}

	public boolean isValidToken(String token) {
		if (null == this.getToken(token)) {
			return false;
		} else {
			return true;
		}
	}

	public Token createToken(String id, String loginName, String sign) {
		Token token = new Token(id, loginName, sign);
		this.addToken(token);
		return token;
	}

	public TokenDao getTokenDao() {
		return tokenDao;
	}

	public void setTokenDao(TokenDao tokenDao) {
		this.tokenDao = tokenDao;
	}

	public void addToken(Token token) {
		this.tokenDao.addToken(token);

	}

	public void removeToken(Token token) {
		this.tokenDao.deleteToken(token.getToken());
	}

}
