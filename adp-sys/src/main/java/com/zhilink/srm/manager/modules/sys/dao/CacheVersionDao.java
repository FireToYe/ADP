/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.CacheVersion;
import com.zhilink.srm.manager.modules.sys.entity.Dict;

/**
 * 字典DAO接口
 * 
 * @author jaray
 * 
 */
@MyBatisDao
public interface CacheVersionDao extends CrudDao<Dict> {

	public CacheVersion getVersionByName(@Param(value = "cacheName") String cacheName);

	public void updateVersion(@Param(value = "cacheName") String cacheName,@Param(value = "version") long version);

	public List<CacheVersion> getVersionList();

}
