/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.entity.SysAppList;
import com.zhilink.srm.manager.modules.sys.dao.SysAppListDao;

/**
 * app清单表Service
 * @author chrisye
 * @version 2017-09-28
 */
@Service
@Transactional(readOnly = true)
public class SysAppListService extends CrudService<SysAppListDao, SysAppList> {
	
	@Autowired
	private SysAppListDao sysAppListDao;

	public SysAppList get(String id) {
		return super.get(id);
	}
	
	public SysAppList getAppInfo(String appid) {
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("appid", appid);
		return sysAppListDao.getAppInfo(params);
	}
	
	public List<SysAppList> findList(SysAppList sysAppList) {
		return super.findList(sysAppList);
	}
	
	public Page<SysAppList> findPage(Page<SysAppList> page, SysAppList sysAppList) {
		return super.findPage(page, sysAppList);
	}
	
	@Transactional(readOnly = false)
	public void save(SysAppList sysAppList) {
		super.save(sysAppList);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysAppList sysAppList) {
		super.delete(sysAppList);
	}
	
}