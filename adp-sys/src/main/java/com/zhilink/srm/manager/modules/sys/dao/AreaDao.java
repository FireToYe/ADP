/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.TreeDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	
	

	/**
	 *  
	 * @param entity
	 * @return
	 */
	public List<Area> findByParentId(@Param(value = "parentId") String parentId);
	
}
