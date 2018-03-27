/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 消息提醒Entity
 * 
 * @author zwhua
 * @version 2017-11-03
 */
public class SysMessage extends DataEntity<SysMessage> {

	private static final long serialVersionUID = 1L;
	private String accepterId; // 接收消息的用户id
	private String type; // 消息类型 '0'普通消息 '1'系统消息
	private String delStatus; // 消息状态'1'已读 '0'未读
	private String title; // 消息标题
	private String content; // 消息内容
	private String sourceMessageId;

	private String attachmentIds; // 附件路劲

	private String attachmentNames;

	private Office company; //归属公司
	private Office office; //归属部门
	private User user;
	private String officeName;
	private String companyName;
	private String receiverName;
	@JsonIgnore
	public String getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(String attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public List<Map<String, String>> getAttachments() {
		List<Map<String, String>> attachments = null;
		if (attachmentIds != null) {

			attachments = new ArrayList<Map<String, String>>();
			if (StringUtils.isNotBlank(attachmentIds) && StringUtils.isNotBlank(attachmentNames)) {
				String[] attaPaths = attachmentIds.split("\\|");
				String[] attaNameArray = attachmentNames.split("\\|");

				HashMap<String, String> attachment = null;
				for (int k = 0; k < attaPaths.length; k++) {
					if (attaPaths[k].length() > 0) {
						attachment = new HashMap<String, String>();
						attachment.put("name", attaNameArray[k]);
						attachment.put("url", attaPaths[k]);
						attachments.add(attachment);
					}
				}
			}
		}
		return attachments;
	}

	@JsonIgnore
	public String getAttachmentNames() {
		return attachmentNames;
	}

	public void setAttachmentNames(String attachmentNames) {
		this.attachmentNames = attachmentNames;
	}

	public SysMessage() {
		super();
	}

	public SysMessage(String id) {
		super(id);
	}

	public String getAccepterId() {
		return accepterId;
	}

	public void setAccepterId(String accepterId) {
		this.accepterId = accepterId;
	}

	@Length(min = 1, max = 200, message = "消息标题长度必须介于 1 和 200 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Length(min = 1, max = 1, message = "消息类型 '0'普通消息 '1'系统消息长度必须介于 1 和 1 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min = 1, max = 1, message = "消息状态'0'已读 '1'未读长度必须介于 1 和 1 之间")
	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	@Length(min = 1, max = 1000, message = "消息内容长度必须介于 1 和 1000 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSourceMessageId() {
		return sourceMessageId;
	}

	public void setSourceMessageId(String sourceMessageId) {
		this.sourceMessageId = sourceMessageId;
	}
	
	public Office getCompany() {
		return company;
	}

	public void setCompany(Office company) {
		this.company = company;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	@Override
	public String toString() {
		return "SysMessage [accepterId=" + accepterId + ", type=" + type + ", delStatus=" + delStatus + ", title="
				+ title + ", content=" + content + ", sourceMessageId=" + sourceMessageId + "]";
	}

}