/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;


import java.util.HashMap;

import com.zhilink.api.adapter.ApiClient;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @author jaray
 * 
 */
@ApiClient()
public interface  ApiProxy {
	@Headers("Content-Type: application/json")
	@RequestLine("POST /{serviceId}")
	public Object post(@Param("serviceId") String serviceId,  Object obj);
	
	
	@Headers("Content-Type: application/json")
	@RequestLine("GET /{serviceId}")
	public Object get(@Param("serviceId") String serviceId,  Object obj);
	
}
