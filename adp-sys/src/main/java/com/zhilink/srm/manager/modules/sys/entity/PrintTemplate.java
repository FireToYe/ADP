/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 打印模板Entity
 * @author sushengshun
 * @version 2017-10-12
 */
public class PrintTemplate extends DataEntity<PrintTemplate> {
	
	private static final long serialVersionUID = 1L;
	private String templateCode;		// 模板标识
	private String templateContent;		// 模板内容
	private String templateParams;		// 测试参数
	private Integer status;		// 启用标识
	private String name;		// 模板名称
	private String description ;		// 描述
	
	public PrintTemplate() {
		super();
	}

	public PrintTemplate(String id){
		super(id);
	}

	@Length(min=1, max=64, message="模板标识长度必须介于 1 和 64 之间")
	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	@NotBlank(message="模板内容不能为空")
	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}
	
	@NotBlank(message="测试参数不能为空")
	public String getTemplateParams() {
		return templateParams;
	}

	public void setTemplateParams(String templateParams) {
		this.templateParams = templateParams;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Length(min=0, max=255, message="模板名称长度必须介于 0 和 255 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description ;
	}

	public void setDescription(String description ) {
		this.description  = description ;
	}
	
}