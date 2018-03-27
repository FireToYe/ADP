/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.Menu;

/**
 * 菜单DAO接口
 * @author jaray
 * 
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {

	public List<Menu> findByParentIdsLike(Menu menu);

	public List<Menu> findByUserId(Menu menu);
	
	public int updateParentIds(Menu menu);
	
	public int updateSort(Menu menu);
	
	public String findMaxId(@Param("leftPartId")String leftPartId,@Param("dbName")String dbName);
	
	public List<Menu> findByParentId(@Param("parentId")String parentId);
	/*
	public void updateNewId(@Param("id") String id,@Param("newId") String newId,@Param("newParentId") String newParentId,@Param("newParentIds") String newParentIds);
	*/

	public void updateById(Map<String,String> map);

}
