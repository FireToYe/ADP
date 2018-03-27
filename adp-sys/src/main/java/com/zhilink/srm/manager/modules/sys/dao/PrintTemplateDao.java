/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.PrintTemplate;

import feign.Param;

/**
 * 打印模板DAO接口
 * @author sushengshun
 * @version 2017-10-12
 */
@MyBatisDao
public interface PrintTemplateDao extends CrudDao<PrintTemplate> {
	public void editStatusToStop(@Param("templateCode") String templateCode);
	
	public List<PrintTemplate> findListNoLike(PrintTemplate printTemplate);
}