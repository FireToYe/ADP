/**
 * 
 */
package com.zhilink.srm.manager.modules.gen.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.gen.entity.GenScheme;

/**
 * 生成方案DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface GenSchemeDao extends CrudDao<GenScheme> {
	
}
