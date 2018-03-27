/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.Map;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.SysAppList;

/**
 * app清单表DAO接口
 * @author chrisye
 * @version 2017-09-28
 */
@MyBatisDao
public interface SysAppListDao extends CrudDao<SysAppList> {
	
	public SysAppList getAppInfo(Map<String, Object> params);
	
}