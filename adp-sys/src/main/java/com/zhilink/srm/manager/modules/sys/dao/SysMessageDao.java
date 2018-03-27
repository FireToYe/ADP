/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhilink.srm.common.persistence.CrudDao;
import com.zhilink.srm.common.persistence.annotation.MyBatisDao;
import com.zhilink.srm.manager.modules.sys.entity.SysMessage;

/**
 * 消息提醒DAO接口
 * @author zwhua
 * @version 2017-11-03
 */
@MyBatisDao
public interface SysMessageDao extends CrudDao<SysMessage> {
	
	//查询某个用户的未读通知的数量
	public int notifyNum(@Param("userId")String userId,@Param("delStatus")String delStatus);
	
	//更新某个用户的信息为已读
	public int updateAll(String delStatus,String userId);
	
	int batchDelete(String[] ids);
	
	//管理员查看所有用户的消息查看情况
	List<SysMessage> findStatusList(SysMessage sysMessage);
	
}