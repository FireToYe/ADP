/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.Log;

/**
 * 日志DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

}
