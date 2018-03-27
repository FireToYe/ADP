/**
 * 
 */
package com.zhilink.sys.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.sys.manager.entity.NewMenu;

/**
 * 菜单DAO接口
 * 
 * @author jaray
 * 
 */
@MyBatisDao
public interface NewMenuDao extends CrudDao<NewMenu> {

	public List<NewMenu> findByParentId(@Param("parentId") String parentId);

	public void updateNewId(@Param("id") String id, @Param("newId") String newId,
			@Param("newParentId") String newParentId, @Param("newParentIds") String newParentIds);

}
