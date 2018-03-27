/**
 * 
 */
package com.zhilink.srm.manager.modules.gen.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.gen.entity.GenTableColumn;

/**
 * 业务表字段DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface GenTableColumnDao extends CrudDao<GenTableColumn> {
	
	public void deleteByGenTableId(String genTableId);
}
