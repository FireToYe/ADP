/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.entity.SysMessage;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;
import com.zhilink.srm.manager.modules.sys.dao.SysMessageDao;
import com.zhilink.srm.manager.modules.sys.dao.SysMessageSendDao;

/**
 * 消息提醒Service
 * @author zwhua
 * @version 2017-11-03
 */
@Service
@Transactional(readOnly = true)
public class SysMessageService extends CrudService<SysMessageDao, SysMessage> {

	@Autowired
	private SysMessageDao sysMessageDao;
	
	@Autowired
	private SysMessageSendDao sysMessageSendDao;
	
	public SysMessage get(String id) {
		return super.get(id);
	}
	
	public List<SysMessage> findList(SysMessage sysMessage) {
		return super.findList(sysMessage);
	}
	
	public Page<SysMessage> findPage(Page<SysMessage> page, SysMessage sysMessage) {
		return super.findPage(page, sysMessage);
	}
	
	public Page<SysMessage> findStatusList(Page<SysMessage> page,SysMessage sysMessage){
		sysMessage.setPage(page);
		page.setList(sysMessageDao.findStatusList(sysMessage));
		return page;
	}
	
	//点击查看消息时更新消息的状态
	@Transactional(readOnly = false)
	public void updateStatus(SysMessage sysMessage){
		sysMessage.setUpdateBy(UserUtils.getUser());
		sysMessage.setUpdateDate(new Date());
		sysMessageDao.update(sysMessage);
	}
	
	@Transactional(readOnly = false)
	public void save(SysMessage sysMessage) {
		// 附件ID，添加附件名称
		if (!StringUtils.isBlank(sysMessage.getAttachmentIds())) {
			String[] paths = sysMessage.getAttachmentIds().split("\\|");
			List<String> fileNames = new ArrayList<String>();
			File file = null;
			for (String path : paths) {
				if (path.length() == 0) {
					fileNames.add("");
				} else {
					file = new File(path);
					fileNames.add(file.getName());
				}
			}
			sysMessage.setAttachmentNames(StringUtils.join(fileNames, "|"));
		}
		super.save(sysMessage);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysMessage sysMessage) {
		super.delete(sysMessage);
	}
	
	@Transactional(readOnly = false)
	public void batchDelete(String[] ids) {
		sysMessageDao.batchDelete(ids);
	}
	
	public int notifyNum(String userId,String delStatus){
		return sysMessageDao.notifyNum(userId,delStatus);
	}
}