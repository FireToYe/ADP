/**
 *  
 */
package com.zhilink.manager.common.token;

import com.zhilink.manager.common.cache.JedisClient;
import com.zhilink.srm.common.config.Constans;

/**
 * 
 * @author jaray
 * 
 */
public class JedisTokenDAO implements TokenDao {
	private JedisClient jedisClient;
	private static final String PREFIX = "token";

	private static String KEY(String key) {
		return Constans.REDIS_PREFIX + PREFIX + key;
	}

	public Token readToken(String tokenKey) {
		Token token = (Token) this.jedisClient.getO(KEY(tokenKey));
		if (token != null) {
			this.jedisClient.expire(KEY(tokenKey), Constans.TOKEN_INVALID_TIME);
			return token;
		} else {
			return null;
		}

	}

	public void deleteToken(String tokenKey) {
		this.jedisClient.del(KEY(tokenKey));

	}

	public void refreshTokenByKey(String tokenKey) {
		this.jedisClient.expire(KEY(tokenKey), Constans.TOKEN_INVALID_TIME);

	}

	public void addToken(Token token) {
		this.jedisClient.set(KEY(token.getToken()), token, Constans.TOKEN_INVALID_TIME);

	}

	public JedisClient getJedisClient() {
		return jedisClient;
	}

	public void setJedisClient(JedisClient jedisClient) {
		this.jedisClient = jedisClient;
	}

}
