package com.zhilink.manager.common.token;

/**
 * 管理token 的所有操作
 * 
 * @author jaray
 */
public interface TokenManager {
	/**
	 * get the token
	 * 
	 * @param token
	 * @return
	 */
	public Token getToken(String token);

	/**
	 * is the token is valid
	 * 
	 * @param token
	 * @return
	 */
	public boolean isValidToken(String token);

	/**
	 * create token
	 */

	public Token createToken(String  Id, String  loginName, String sign);
	
	public void addToken(Token  token);
	
	
	public void removeToken(Token token);

}
