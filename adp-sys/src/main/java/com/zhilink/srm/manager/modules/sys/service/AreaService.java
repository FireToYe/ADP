/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.manager.framework.common.comstans.Constants;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.service.TreeService;
import com.zhilink.srm.manager.modules.sys.dao.AreaDao;
import com.zhilink.srm.manager.modules.sys.entity.Area;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 区域Service
 * 
 * @author jaray
 * 
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {
	@Autowired
	CacheVersionService cacheVersionService;

	public List<Area> findAll() {
		return UserUtils.getAreaList();
	}

	public List<Area> findByParentId(String parentId) {
		if (StringUtils.isEmpty(parentId)) {
			parentId = "0";
		}
		return this.dao.findByParentId(parentId);

	}

	@Transactional(readOnly = false)
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		this.cacheVersionService.updateVersion(Constants.AREA_CACHE_VERSION_KEY);
	}

	@Transactional(readOnly = false)
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		this.cacheVersionService.updateVersion(Constants.AREA_CACHE_VERSION_KEY);
	}

}
