/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.AppApply;

/**
 * app授权申请表DAO接口
 * @author chrisye
 * @version 2017-09-28
 */
@MyBatisDao
public interface AppApplyDao extends CrudDao<AppApply> {
	int updateStatus(AppApply appApply);
	
	public void updateApply(AppApply appApply);
	
	public AppApply selectByRelateUser(String relateUser);
	
	public void deleteByPhy(String openid);

}