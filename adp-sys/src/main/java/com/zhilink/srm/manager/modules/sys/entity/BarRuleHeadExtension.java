/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.entity;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.validator.constraints.Length;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 条码编码规则设置单头档Entity
 * @author lijiaxuan
 * @version 2017-09-04
 */
public class BarRuleHeadExtension extends DataEntity<BarRuleHeadExtension> {
	
	private static final long serialVersionUID = 1L;
	private String barRuleNo;		// 编码规则编号
	private String barRuleName;		// 编码规则名称/说明
	private String barLotNo;		// 条码批次构成
	private String codeTotal;		// 编码总段数
	private String isNorm;		// 规范否
	private String isNumToBar;		// 数量是否写入条码
	private String barType;		// 条码类型
	private String createOffice;		// 资料建立部门
	private String ent;		// 企业代码
	private String deptName;		// 资料所属部门
	private String owner;		// 资料所有者
	private String site;		// 营运据点
	private String status;		// 状态码
	private String extension;		// 拓展
	
	private Map<String, String> mapExtension;
	
	public BarRuleHeadExtension() {
		super();
	}

	public BarRuleHeadExtension(String id){
		super(id);
	}

	@Length(min=1, max=10, message="编码规则编号长度必须介于 1 和 10 之间")
	public String getBarRuleNo() {
		return barRuleNo;
	}

	public void setBarRuleNo(String barRuleNo) {
		this.barRuleNo = barRuleNo;
	}
	
	@Length(min=0, max=10, message="条码批次构成长度必须介于 0 和 10 之间")
	public String getBarLotNo() {
		return barLotNo;
	}

	public void setBarLotNo(String barLotNo) {
		this.barLotNo = barLotNo;
	}
	
	public String getCodeTotal() {
		return codeTotal;
	}

	public void setCodeTotal(String codeTotal) {
		this.codeTotal = codeTotal;
	}
	
	@Length(min=0, max=1, message="规范否长度必须介于 0 和 1 之间")
	public String getIsNorm() {
		return isNorm;
	}

	public void setIsNorm(String isNorm) {
		this.isNorm = isNorm;
	}
	
	@Length(min=0, max=1, message="数量是否写入条码长度必须介于 0 和 1 之间")
	public String getIsNumToBar() {
		return isNumToBar;
	}

	public void setIsNumToBar(String isNumToBar) {
		this.isNumToBar = isNumToBar;
	}
	
	@Length(min=0, max=1, message="条码类型长度必须介于 0 和 1 之间")
	public String getBarType() {
		return barType;
	}

	public void setBarType(String barType) {
		this.barType = barType;
	}
	
	@Length(min=0, max=10, message="资料建立部门长度必须介于 0 和 10 之间")
	public String getCreateOffice() {
		return createOffice;
	}

	public void setCreateOffice(String createOffice) {
		this.createOffice = createOffice;
	}
	
	public String getEnt() {
		return ent;
	}

	public void setEnt(String ent) {
		this.ent = ent;
	}
	
	@Length(min=0, max=10, message="资料所属部门长度必须介于 0 和 10 之间")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@Length(min=0, max=20, message="资料所有者长度必须介于 0 和 20 之间")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	@Length(min=0, max=10, message="状态码长度必须介于 0 和 10 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=100, message="规则名称/说明长度必须介于 0 和 256之间")
	public String getBarRuleName() {
		return barRuleName;
	}

	public void setBarRuleName(String barRuleName) {
		this.barRuleName = barRuleName;
	}
	
	/////////////////////////////华丽的分割线////////////////////////////////////////////////////////////////
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension.trim();
		if (this.extension != null && this.extension.startsWith("{")) {
			this.mapExtension = new HashMap<String, String>();
			try {
				Gson gson = new Gson();
				mapExtension = gson.fromJson(this.extension, new TypeToken<Map<String, String>>() { }.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Map<String, String> getMapExtension() {
		return mapExtension;
	}

	public void setMapExtension(Map<String, String> mapExtension) {
		this.mapExtension = mapExtension;
		try {
			Gson gson = new Gson();
			this.extension = gson.toJson(this.mapExtension);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取出某个扩展键值的内容
	 * @param key
	 * @return
	 */
	public String getExtensionValue(String key) {
		if (this.mapExtension == null || !this.mapExtension.containsKey(key)) {
			return null;
		}
		return this.mapExtension.get(key);
	}

	/**
	 * 添加扩展键值
	 * @param key
	 * @param value
	 */
	public void addExtensionValue(String key, String value) {
		if (this.mapExtension == null) {
			this.mapExtension = new HashMap<String, String>();
		}
		this.mapExtension.put(key, value);
		try {
			Gson gson = new Gson();
			this.extension = gson.toJson(this.mapExtension);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除扩展键值
	 * @param key
	 */
	public void removeExtensionKey(String key) {
		if (this.mapExtension != null && this.mapExtension.containsKey(key)) {
			this.mapExtension.remove(key);
		}
		if (this.mapExtension == null) {
			this.extension = null;
		} else {
			try {
				Gson gson = new Gson();
				this.extension = gson.toJson(this.mapExtension);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/*public static void main(String[] args) {
		
		Map<String, String> map=new HashMap<String, String>();
		for(int i=0;i<100;i++){
			map.put("key"+i, "value"+i);
		}
		Gson gson = new Gson();
		String str = gson.toJson(map);
		//System.out.println(str);
		
		long t1=System.currentTimeMillis();
		BarRuleHead rbh=new BarRuleHead();
		rbh.setExtension(str);
		System.out.println(rbh.getExtensionValue("key30"));
		rbh.addExtensionValue("abc", "123");
		System.out.println(rbh.getExtension());
		System.out.println("time="+(System.currentTimeMillis()-t1));
		
	}*/
	
}