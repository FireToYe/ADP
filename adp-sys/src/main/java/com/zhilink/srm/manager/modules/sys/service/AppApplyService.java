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
import com.zhilink.srm.manager.modules.sys.entity.AppApply;
import com.zhilink.srm.manager.modules.sys.dao.AppApplyDao;

/**
 * app授权申请表Service
 * @author chrisye
 * @version 2017-09-28
 */
@Service
@Transactional(readOnly = true)
public class AppApplyService extends CrudService<AppApplyDao, AppApply> {

	@Autowired
	AppApplyDao appApplyDao;
	public AppApply get(String id) {
		return super.get(id);
	}
	
	public List<AppApply> findList(AppApply appApply) {
		return super.findList(appApply);
	}
	
	public Page<AppApply> findPage(Page<AppApply> page, AppApply appApply) {
		return super.findPage(page, appApply);
	}
	
	@Transactional(readOnly = false)
	public void save(AppApply appApply) {
		super.save(appApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(AppApply appApply) {
		super.delete(appApply);
	}
	@Transactional(readOnly = false)
	public void updateStatus(AppApply appApply) {
		appApplyDao.updateStatus(appApply);
	}
	
	@Transactional(readOnly = false)
	public void insertApply(AppApply appApply) {
		appApplyDao.insert(appApply);
	}
	
	@Transactional(readOnly = false)
	public void updateApply(AppApply appApply) {
		appApplyDao.updateApply(appApply);
	}
	
	public AppApply selectByRelateUser(String relateUser){
		return dao.selectByRelateUser(relateUser);
	}
	@Transactional(readOnly = false)
	public void deleteByPhy(String openid){
		dao.deleteByPhy(openid);
	}
}