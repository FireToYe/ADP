/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.SysAppVersion;

/**
 * 应用版本管理DAO接口
 * @author keris
 * @version 2017-10-13
 */
@MyBatisDao
public interface SysAppVersionDao extends CrudDao<SysAppVersion> {

	SysAppVersion getAppname(String appname);

}