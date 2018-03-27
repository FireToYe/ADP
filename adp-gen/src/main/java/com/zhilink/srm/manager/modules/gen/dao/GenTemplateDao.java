/**
 * 
 */
package com.zhilink.srm.manager.modules.gen.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.gen.entity.GenTemplate;

/**
 * 代码模板DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface GenTemplateDao extends CrudDao<GenTemplate> {
	
}
