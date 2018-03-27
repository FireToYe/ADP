package com.zhilink.adp.client.api;

import com.zhilink.adp.client.model.Token;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface BaseInterface {
		
	//@Headers("content-type: application/x-www-form-urlencoded")
	//@Headers("content-type: application/json")
	@RequestLine("POST /api/login?loginName={loginName}&password={password}")
    public Token getTokenByLogin(@Param("loginName") String loginName, @Param("password") String password);
	
	

}
