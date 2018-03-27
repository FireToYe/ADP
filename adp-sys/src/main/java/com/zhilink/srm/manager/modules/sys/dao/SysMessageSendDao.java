/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;

/**
 * 消息发送DAO接口
 * @author zwhua
 * @version 2017-11-06
 */
@MyBatisDao
public interface SysMessageSendDao extends CrudDao<SysMessageSend> {
	
}