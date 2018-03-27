/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.sys.dao.SysMessageDao;
import com.zhilink.srm.manager.modules.sys.dao.SysMessageSendDao;
import com.zhilink.srm.manager.modules.sys.dao.UserDao;
import com.zhilink.srm.manager.modules.sys.entity.SysMessage;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;

/**
 * 消息发送Service
 * 
 * @author zwhua
 * @version 2017-11-06
 */
@Service
@Transactional(readOnly = true)
public class SysMessageSendService extends CrudService<SysMessageSendDao, SysMessageSend> {

	@Autowired
	private UserDao userDao;
	@Autowired
	private SysMessageDao sysMessageDao;

	public SysMessageSend get(String id) {
		SysMessageSend sysMessageSend = super.get(id);
		String[] userIds = sysMessageSend.getReceivers().trim().replaceAll(" ", "").split(",");
		String userNames = userDao.getNameById(userIds);
		sysMessageSend.setReceivers(userNames);
		return sysMessageSend;
	}

	public List<SysMessageSend> findList(SysMessageSend sysMessageSend) {
		return super.findList(sysMessageSend);
	}

	public Page<SysMessageSend> findPage(Page<SysMessageSend> page, SysMessageSend sysMessageSend) {
		return super.findPage(page, sysMessageSend);
	}

	@Transactional(readOnly = false)
	public void save(SysMessageSend sysMessageSend) {
		super.save(sysMessageSend);
	}

	// 发送消息
	@Transactional(readOnly = false)
	public boolean sendMessage(SysMessageSend sysMessageSend) {
		String users = "";
		List<String> userList = new ArrayList<String>();
		if ("0".equals(sysMessageSend.getTarget())) {
			// 全部
			List tmpUserList = userDao.findAllUserId('0');
			userList.addAll(tmpUserList);
		} else if ("1".equals(sysMessageSend.getTarget())) {
			// 组织
			String[] officeIds = sysMessageSend.getReceivers().split(",");
			String[] officePids = sysMessageSend.getPids().split(",");
			for (int i = 0; i < officeIds.length; i++) {
				if ("0".equals(officePids[i])) {
					List tmpUserList = userDao.findUserIdByOfficeId("", officeIds[i], '0');
					userList.addAll(tmpUserList);
				} else {
					List tmpUserList = userDao.findUserIdByOfficeId(officeIds[i], "", '0');
					userList.addAll(tmpUserList);
				}
			}
		} else if ("2".equals(sysMessageSend.getTarget())) {
			// 角色
			String[] roleList = sysMessageSend.getReceivers().trim().replaceAll(" ", "").split(",");
			List tmpUserList = userDao.findUserByRole(roleList);
			userList.addAll(tmpUserList);
		} else if ("3".equals(sysMessageSend.getTarget())) {
			// 用户
			String[] receivers = sysMessageSend.getReceivers().trim().replaceAll(" ", "").split(",");
			userList.addAll(java.util.Arrays.asList(receivers));
		}
		// 保存发送方的消息
		if (userList.isEmpty()) {
			return false;
		} else {
			sysMessageSend.setReceivers(StringUtils.join(userList, ","));
			sysMessageSend.setCount(String.valueOf(userList.size()));
		}
		super.save(sysMessageSend);

		// 保存接收方的消息
		SysMessage sysMessage = new SysMessage();
		sysMessage.setAccepterId(sysMessageSend.getReceivers());
		sysMessage.setTitle(sysMessageSend.getTitle());
		sysMessage.setContent(sysMessageSend.getContent());
		sysMessage.setSourceMessageId(sysMessageSend.getId());
		sysMessage.setType(sysMessageSend.getType());
		sysMessage.setAttachmentIds(sysMessageSend.getAttachmentIds());

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

		for (String strUser : userList) {
			sysMessage.setIsNewRecord(true);
			sysMessage.setAccepterId(strUser);
			sysMessage.setDelStatus("0");
			sysMessage.preInsert();
			sysMessageDao.insert(sysMessage);
		}
		return true;

	}

	@Transactional(readOnly = false)
	public void delete(SysMessageSend sysMessageSend) {
		super.delete(sysMessageSend);
	}
}