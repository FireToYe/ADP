/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.db.entity;

import org.hibernate.validator.constraints.Length;
import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 数据库备份方案Entity
 * @author ben
 * @version 2017-12-13
 */
public class DbBackupScheme extends DataEntity<DbBackupScheme> {
	
	private static final long serialVersionUID = 1L;
	private String schemeName;		// 方案名称
	private String backupTables;		// 备份表(用,号分隔)
	private String extend;		// 扩展内容
	private String jobStatus;
	private String jobId;
	private String cronExpression;
	private String saveCount;
	public DbBackupScheme() {
		super();
	}

	public DbBackupScheme(String id){
		super(id);
	}

	@Length(min=1, max=32, message="方案名称长度必须介于 1 和 32 之间")
	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}
	
	@Length(min=1, max=1000, message="备份表(用,号分隔)长度必须介于 1 和 1000 之间")
	public String getBackupTables() {
		return backupTables;
	}

	public void setBackupTables(String backupTables) {
		this.backupTables = backupTables;
	}
	
	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getSaveCount() {
		return saveCount;
	}

	public void setSaveCount(String saveCount) {
		this.saveCount = saveCount;
	}
	
}