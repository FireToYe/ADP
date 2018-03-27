/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.zhilink.srm.manager.modules.sys.dao.ApiProxy;

/**
 * 日志Service
 * 
 * @author jaray
 * 
 */
@Service
@Transactional(readOnly = true)
public class ApiProxyService {
	public static Gson gson = new Gson();
	@Autowired(required=false)
	private ApiProxy apiProxy;
	
	public  Object doGet(String serviceId,String jsonObj){
		return this.apiProxy.get(serviceId, gson.fromJson(jsonObj, Object.class));
	}
	public  Object doPost(String serviceId,String jsonObj){
		return this.apiProxy.post(serviceId, gson.fromJson(jsonObj, Object.class));
	}
	
	

}
