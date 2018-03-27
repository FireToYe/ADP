package com.zhilink.srm.manager.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.dao.SysExtendWordDao;
import com.zhilink.srm.manager.modules.sys.entity.SysExtendWord;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;

/**
 * 扩展字段Service
 * @version 2017-12-20
 */
@Service
@Transactional(readOnly = true)
public class SysExtendWordService extends CrudService<SysExtendWordDao, SysExtendWord> {
	
	@Autowired
	private SysExtendWordDao sysExtendWordDao;
	

	public SysExtendWord get(String id) {
		return super.get(id);
	}
	
	public List<SysExtendWord> findList(SysExtendWord sysExtendWord) {
		return super.findList(sysExtendWord);
	}
	
	public Page<SysExtendWord> findPage(Page<SysExtendWord> page, SysExtendWord sysExtendWord) {
		return super.findPage(page, sysExtendWord);
	}
	
	@Transactional(readOnly = false)
	public void save(SysExtendWord sysExtendWord) {
		super.save(sysExtendWord);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysExtendWord sysExtendWord) {
		super.delete(sysExtendWord);
	}
	
	public List<SysExtendWord> findKeyList(){
		return sysExtendWordDao.findKeyList();
	}
	
	public int existSameData(String key,String name,String id){
		return sysExtendWordDao.existSameData(key, name, id);
	}
}