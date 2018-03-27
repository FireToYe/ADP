/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 新闻通告Entity
 * 
 * @author an48huf
 * @version 2017-11-07
 */
public class News extends DataEntity<News> {

	private static final long serialVersionUID = 1L;
	private String id; // 编号ID
	private String subject; // 新闻标题
	private String content; // 新闻内容
	private int clickCount; // 点击数
	private String attachmentId; // 附件ID串
	private String attachmentName; // 附件名称串
	private String typeId; // 新闻类型
	private String publish; // 发布标识(0-未发布 隐藏,1-已发布,显示,2-已终止)
	private String top; // 是否置顶(0-否,1-是)
	private String subjectColor; // 新闻标题颜色
	private String keyword; // 内容关键词
	private String accepterId; // 接收消息的用户id
	private String delStatus; // 消息状态'1'已读 '0'未读

	public String getAccepterId() {
		return accepterId;
	}

	public void setAccepterId(String accepterId) {
		this.accepterId = accepterId;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	/**
	 * 附件列表
	 */
	private List<HashMap<String,String>> attachments;

	public List<HashMap<String,String>> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<HashMap<String,String>> attachments) {
		this.attachments = attachments;
	}

	public News() {
		super();
	}

	public News(String id) {
		super(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Length(min = 1, max = 200, message = "新闻标题长度必须介于 1 和 200 之间")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getSubjectColor() {
		return subjectColor;
	}

	public void setSubjectColor(String subjectColor) {
		this.subjectColor = subjectColor;
	}

/*	@Length(min = 1, max = 1000, message = "内容关键词长度必须介于 1 和 1000之间")*/
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}