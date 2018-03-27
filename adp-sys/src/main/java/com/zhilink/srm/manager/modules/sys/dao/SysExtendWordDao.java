package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.SysExtendWord;

/**
 * 扩展字段接口
 * @version 2017-12-20
 */
@MyBatisDao
public interface SysExtendWordDao extends CrudDao<SysExtendWord> {
	
	//查找所有的key列表
	List<SysExtendWord> findKeyList();
	
	//查找是否存在相同的key和name
	int existSameData(@Param("key") String key, @Param("name") String name, @Param("id") String id);
	
}