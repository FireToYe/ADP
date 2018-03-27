/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import oracle.net.aso.s;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.entity.SysProperties;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.dao.SysPropertiesDao;

/**
 * 系统参数Service
 * @author chrisye
 * @version 2018-01-16
 */
@Service
@Transactional(readOnly = true)
public class SysPropertiesService extends CrudService<SysPropertiesDao, SysProperties> {

	public SysProperties get(String id) {
		return super.get(id);
	}
	
	public List<SysProperties> findList(SysProperties sysProperties) {
		return super.findList(sysProperties);
	}
	
	public Page<SysProperties> findPage(Page<SysProperties> page, SysProperties sysProperties) {
		return super.findPage(page, sysProperties);
	}
	
	@Transactional(readOnly = false)
	public void save(SysProperties sysProperties) {
		super.save(sysProperties);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysProperties sysProperties) {
		super.delete(sysProperties);
	}
	
	public SysProperties checkOnly(SysProperties sysProperties) {
		return dao.checkOnly(sysProperties);
	}
	/**
	 * 如果key值存在则更新，不存在则插入
	 * @param key
	 * @param value
	 * @param flag 表示是否可在页面修改，Y为可在页面修改
	 */
	@Transactional(readOnly = false)
	public void saveByKeyValue(String key,String value,boolean flag){
		String modifyBy = "N";
		if(flag){
			modifyBy = "Y";
		}
		SysProperties sysProperties = new SysProperties(key,value,modifyBy);
		SysProperties checkProperties = checkOnly(sysProperties);
		if(checkProperties!=null){
			sysProperties.setId(checkProperties.getId());
			sysProperties.preUpdate();
			if(sysProperties.getUpdateBy()==null){
				sysProperties.setUpdateBy(new User(""));
			}
			dao.update(sysProperties);
		}else{
			sysProperties.preInsert();
			if(sysProperties.getCreateBy()==null){
				sysProperties.setCreateBy(new User(""));
			}
			if(sysProperties.getUpdateBy()==null){
				sysProperties.setUpdateBy(new User(""));
			}
			dao.insert(sysProperties);
		}
	}
	@Transactional(readOnly = false)
	public void saveByKeyValue(String key,String value){
		saveByKeyValue(key,value,false);
	}
	/**
	 * 会依次保存Map，如果存在则更新，
	 * @param map
	 * @param flag 表示是否可在页面修改，Y为可在页面修改
	 */
	@Transactional(readOnly = false)
	public void saveByMap(Map<String,String> map,boolean flag){
		Set<Entry<String,String>> set = map.entrySet();
		for(Entry<String,String> entry : set){
			saveByKeyValue(entry.getKey(),entry.getValue(),flag);
		}
	}
	@Transactional(readOnly = false)
	public void saveByMap(Map<String,String> map){
		saveByMap(map,false);
	}
	
	public String getValue(String key){
		SysProperties sysProperties = new SysProperties();
		sysProperties.setKey(key);
		SysProperties checkProperties = checkOnly(sysProperties);
		if(checkProperties!=null){
			return checkProperties.getValue();
		}
		return null;
	}
}