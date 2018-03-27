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
import com.zhilink.srm.manager.modules.sys.entity.SysAppSql;
import com.zhilink.srm.manager.modules.sys.dao.SysAppSqlDao;

/**
 * 应用SQL管理Service
 * @author xh
 * @version 2017-12-12
 */
@Service
@Transactional(readOnly = true)
public class SysAppSqlService extends CrudService<SysAppSqlDao, SysAppSql> {
	
	@Autowired
	private SysAppSqlDao sysAppSqlDao;
	
	public SysAppSql get(String id) {
		return super.get(id);
	}
	
	public List<SysAppSql> findList(SysAppSql sysAppSql) {
		return super.findList(sysAppSql);
	}
	
	public Page<SysAppSql> findPage(Page<SysAppSql> page, SysAppSql sysAppSql) {
		return super.findPage(page, sysAppSql);
	}
	
	@Transactional(readOnly = false)
	public void save(SysAppSql sysAppSql) {
		super.save(sysAppSql);
	}

	@Transactional(readOnly = false)
	public void updateErrorSql(String id) {
		sysAppSqlDao.updateErrorSql(id);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysAppSql sysAppSql) {
		super.delete(sysAppSql);
	}
	
}