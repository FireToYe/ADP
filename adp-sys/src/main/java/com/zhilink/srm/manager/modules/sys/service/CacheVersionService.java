/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.manager.framework.common.comstans.Constants;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.dao.CacheVersionDao;
import com.zhilink.srm.manager.modules.sys.entity.CacheVersion;
import com.zhilink.srm.manager.modules.sys.entity.Dict;

/**
 * 字典Service
 * 
 * @author jaray
 * 
 */
@Service
@Transactional(readOnly = true)
public class CacheVersionService extends CrudService<CacheVersionDao, Dict> {

	public CacheVersion getVersionByName(String cacheName) {
		CacheVersion c = (CacheVersion) CacheUtils.get(Constants.CACHE_VERSION_KEY);
		if (null == c) {
			c = this.dao.getVersionByName(cacheName);
			CacheUtils.put(Constants.CACHE_VERSION_KEY, c);
		}
		return c;
	}

	public List<CacheVersion> getVersionList() {
		List<CacheVersion> cl = (List<CacheVersion>) CacheUtils.get(Constants.CACHE_VERSION_KEY);
		if (null == cl || cl.size() == 0) {
			cl = this.dao.getVersionList();
			CacheUtils.put(Constants.CACHE_VERSION_KEY, cl);
		}
		return cl;
	}

	@Transactional(readOnly = false)
	public void updateVersion(String cVName) {
		long version = System.currentTimeMillis();
		this.dao.updateVersion(cVName, version);
		CacheUtils.remove(Constants.CACHE_VERSION_KEY);
	}

}
