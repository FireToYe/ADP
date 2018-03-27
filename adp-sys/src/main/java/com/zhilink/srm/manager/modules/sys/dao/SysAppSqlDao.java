/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.SysAppSql;

/**
 * 应用SQL管理DAO接口
 * @author xh
 * @version 2017-12-12
 */
@MyBatisDao
public interface SysAppSqlDao extends CrudDao<SysAppSql> {
	void updateErrorSql(String id);
}