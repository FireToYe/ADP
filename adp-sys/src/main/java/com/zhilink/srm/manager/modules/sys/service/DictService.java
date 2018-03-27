/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.manager.framework.common.comstans.Constants;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.dao.DictDao;
import com.zhilink.srm.manager.modules.sys.entity.Dict;
import com.zhilink.srm.manager.modules.sys.utils.DictUtils;

/**
 * 字典Service
 * 
 * @author jaray
 * 
 */
@Service
@Transactional(readOnly = true)
public class DictService extends CrudService<DictDao, Dict> {
	@Autowired
	CacheVersionService cacheVersionService;

	/**
	 * 查询字段类型列表
	 * 
	 * @return
	 */
	public List<String> findTypeList() {
		return dao.findTypeList(new Dict());
	}

	@Transactional(readOnly = false)
	public void save(Dict dict) {
		super.save(dict);
		this.cacheVersionService.updateVersion(Constants.DICT_CACHE_VERSION_KEY);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
		this.cacheVersionService.updateVersion(Constants.DICT_CACHE_VERSION_KEY);
	}

	public List<Dict> getTypeDict(String typeName) {

		return this.dao.findTypeDict(typeName);

	}

}
