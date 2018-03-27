/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.entity;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 字典Entity
 * @author jaray
 * 
 */
public class Dict extends DataEntity<Dict> {

	private static final long serialVersionUID = 1L;
	private String value;	// 数据值
	private String label;	// 标签名
	private String type;	// 类型
	private String description;// 描述
	private Integer sort;	// 排序
	private String parentId;//父Id
	
	//国际化语言
	private String labelEnUS; //英文名称
	private String labelZhTW; //繁体名称
	private String labelZhCN; //中文名称

	
	
	public String getLabelEnUS() {
		return labelEnUS;
	}

	public void setLabelEnUS(String labelEnUS) {
		this.labelEnUS = labelEnUS;
	}

	public String getLabelZhTW() {
		return labelZhTW;
	}

	public void setLabelZhTW(String labelZhTW) {
		this.labelZhTW = labelZhTW;
	}

	public String getLabelZhCN() {
		return labelZhCN;
	}

	public void setLabelZhCN(String labelZhCN) {
		this.labelZhCN = labelZhCN;
	}

	public Dict() {
		super();
	}
	
	public Dict(String id){
		super(id);
	}
	
	public Dict(String value, String label){
		this.value = value;
		this.label = label;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@XmlAttribute
	@Length(min=1, max=100)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Length(min=1, max=100)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	@Length(min=0, max=100)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Length(min=1, max=100)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
	public String toString() {
		return label;
	}
}