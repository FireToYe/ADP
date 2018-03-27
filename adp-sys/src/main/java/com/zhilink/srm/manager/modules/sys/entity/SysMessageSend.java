/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 消息发送Entity
 * @author zwhua
 * @version 2017-11-06
 */
public class SysMessageSend extends DataEntity<SysMessageSend> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String content;		// 内容
	private String target;		// 目标对象(0全部 1按组织 2按角色 3按会员)
	private String count;		// 目标人数
	private String type;		//消息类型(0普通消息 1系统消息)
	private String status;		// 状态(0未发送 1已发送)
	private String receivers;		// 发送人
	private String pids;		//存放组织的组织的pid,根据这个pids来判断用户是company还是office
	
	private String attachmentIds; //附件路劲
	
	private String attachmentNames;//附件名称
	
	public String getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(String attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public String getAttachmentNames() {
		return attachmentNames;
	}

	public void setAttachmentNames(String attachmentNames) {
		this.attachmentNames = attachmentNames;
	}

	public SysMessageSend() {
		super();
	}

	public SysMessageSend(String id){
		super(id);
	}

	@Length(min=0, max=100, message="标题长度必须介于 0 和 100 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=256, message="内容长度必须介于 0 和 256 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=4, message="目标对象(0全部 1按组织 2按角色 3按会员)长度必须介于 0 和 4 之间")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	@Length(min=0, max=11, message="目标人数长度必须介于 0 和 11 之间")
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	@Length(min=0, max=4, message="状态(0未发送 1已发送)长度必须介于 0 和 4 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getReceivers() {
		return receivers;
	}
	
	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}
	
}