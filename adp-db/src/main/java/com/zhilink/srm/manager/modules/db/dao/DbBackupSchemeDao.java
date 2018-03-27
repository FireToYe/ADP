/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.db.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.db.entity.DbBackupScheme;

/**
 * 数据库备份方案DAO接口
 * @author ben
 * @version 2017-12-13
 */
@MyBatisDao
public interface DbBackupSchemeDao extends CrudDao<DbBackupScheme> {
	public DbBackupScheme checkOnly(DbBackupScheme dbBackupScheme);
}