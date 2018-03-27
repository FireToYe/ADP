/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.SysProperties;

/**
 * 系统参数DAO接口
 * @author chrisye
 * @version 2018-01-16
 */
@MyBatisDao
public interface SysPropertiesDao extends CrudDao<SysProperties> {
	public SysProperties checkOnly(SysProperties sysProperties);
}