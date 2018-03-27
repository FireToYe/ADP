/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.service.TreeService;
import com.zhilink.srm.manager.modules.sys.dao.OfficeDao;
import com.zhilink.srm.manager.modules.sys.entity.Office;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

import de.schlichtherle.license.LicenseContent;

/**
 * 机构Service
 * 
 * @author jaray
 * 
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	public List<Office> findAll() {
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll) {
		if (isAll != null && isAll) {
			return UserUtils.getOfficeAllList();
		} else {
			return UserUtils.getOfficeList();
		}
	}

	@Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		if (office != null) {
			office.setParentIds(office.getParentIds() + "%");
			return dao.findByParentIdsLike(office);
		}
		return new ArrayList<Office>();
	}
	@Transactional(readOnly = true)
	public int selectLevelCompanyCount() {
		return dao.selectLevelCompanyCount();
	}
	@Transactional(readOnly = false)
	public void save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}

	/**
	 * 判断机构是否存在
	 * 目前sql写死只以机构编码判断唯一
	 * @param office
	 * @return
	 */
	public int judgeExist(String code) {
		int count = dao.judgeExist(code);		
		return count;
		
	}
	
	@Transactional(readOnly = false)
	public void importSave(List<Office> fList) {
		
		for(Office office : fList) {
			super.save(office);
		}
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		
	}
	
	/**
	 * 查询供应商
	 * @param office
	 * @return
	 */
	public List<Office> findSupplier(Office office) {
		List<Office> list = dao.findSupplier(office);
		return list;
	}
	
	public int isExistSameCode(String code,String id){
		return dao.isExistSameCode(code, id);
	}
}
