/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.dao.SysAppVersionDao;
import com.zhilink.srm.manager.modules.sys.entity.SysAppVersion;

/**
 * 应用版本管理Service
 * @author keris
 * @version 2017-10-13
 */
@Service
@Transactional(readOnly = true)
public class SysAppVersionService extends CrudService<SysAppVersionDao, SysAppVersion> {

	@Autowired 
	private SysAppVersionDao sysAppVersionDao;
	public SysAppVersion get(String id) {
		return super.get(id);
	}
	
	public List<SysAppVersion> findList(SysAppVersion sysAppVersion) {
		return super.findList(sysAppVersion);
	}
	
	public Page<SysAppVersion> findPage(Page<SysAppVersion> page, SysAppVersion sysAppVersion) {
		return super.findPage(page, sysAppVersion);
	}
	
	@Transactional(readOnly = false)
	public void save(SysAppVersion sysAppVersion) {
		super.save(sysAppVersion);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysAppVersion sysAppVersion) {
		super.delete(sysAppVersion);
	}

	public SysAppVersion getAppname(String appname) {
		return sysAppVersionDao.getAppname(appname);
	}

	
	
}