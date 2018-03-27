/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.db.service;

import java.util.List;

import oracle.net.aso.j;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhilink.manager.common.utils.Exceptions;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.service.CrudService;
import com.zhilink.srm.manager.modules.db.entity.DbBackupScheme;
import com.zhilink.srm.manager.modules.db.dao.DbBackupSchemeDao;
import com.zhilink.srm.manager.modules.job.dao.JobDao;
import com.zhilink.srm.manager.modules.job.entity.JobEntity;
import com.zhilink.srm.manager.modules.job.service.JobService;

/**
 * 数据库备份方案Service
 * @author ben
 * @version 2017-12-13
 */
@Service
@Transactional(readOnly = true)
public class DbBackupSchemeService extends CrudService<DbBackupSchemeDao, DbBackupScheme> {
	@Autowired
	private JobDao jobdao;
	
	@Autowired
	private JobService jobService;
	public DbBackupScheme get(String id) {
		return super.get(id);
	}
	
	public List<DbBackupScheme> findList(DbBackupScheme dbBackupScheme) {
		return super.findList(dbBackupScheme);
	}
	
	public Page<DbBackupScheme> findPage(Page<DbBackupScheme> page, DbBackupScheme dbBackupScheme) {
		return super.findPage(page, dbBackupScheme);
	}
	
	@Transactional(readOnly = false)
	public void save(DbBackupScheme dbBackupScheme) {
		createJobToDbBackupScheme(dbBackupScheme);
		super.save(dbBackupScheme);
	}
	
	@Transactional(readOnly = false)
	public void delete(DbBackupScheme dbBackupScheme) {
		if(!StringUtils.isEmpty(dbBackupScheme.getJobId())){
			try {
				JobEntity jobEntity = jobdao.get(dbBackupScheme.getJobId());
				if(jobEntity!=null&&!"0".equals(jobEntity.getDelFlag())){
					jobService.deleteJob(jobEntity);
					jobService.delete(jobEntity);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(Exceptions.getStackTraceAsString(e));
			}
		}
		super.delete(dbBackupScheme);
	}
	
	public DbBackupScheme checkOnly(DbBackupScheme dbBackupScheme){
		return dao.checkOnly(dbBackupScheme);
	}
	
	public void createJobToDbBackupScheme(DbBackupScheme dbBackupScheme){
		if(!StringUtils.isEmpty(dbBackupScheme.getJobId())){
			if("1".equals(dbBackupScheme.getJobStatus())){
				JobEntity jobEntity = jobdao.get(dbBackupScheme.getJobId());
				if(jobEntity!=null&&"0".equals(jobEntity.getDelFlag())){
					jobEntity.setJobState("1");
					try {
						jobService.updateState(jobEntity);
						jobService.addJob(jobEntity);
					} catch (SchedulerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(Exceptions.getStackTraceAsString(e));
					}
					return;
				}else{
					dbBackupScheme.setJobId("");
				}
			}
			if("0".equals(dbBackupScheme.getJobStatus())){
				JobEntity jobEntity = jobdao.get(dbBackupScheme.getJobId());
				if(jobEntity!=null){
					jobEntity.setJobState("0");
					try {
						jobService.updateState(jobEntity);
						jobService.deleteJob(jobEntity);
					} catch (SchedulerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(Exceptions.getStackTraceAsString(e));
					}
					return;
				}
			}
		}
		if("1".equals(dbBackupScheme.getJobStatus())&&!StringUtils.isEmpty(dbBackupScheme.getCronExpression())){
			JobEntity jobEntity = new JobEntity();
			jobEntity.setJobGroup("数据备份方案");
			jobEntity.setJobName(dbBackupScheme.getSchemeName());
			jobEntity.setCronExpression(dbBackupScheme.getCronExpression());
			jobEntity.setJobState("1");
			jobEntity.setIsConcurrent("0");
			jobEntity.setExecuteType("2");
			jobEntity.setParameter(dbBackupScheme.getSchemeName());
			jobEntity.setSpringId("backupController");
			jobEntity.setMethodName("doBackUpByJob");
			jobEntity.setDependLastTime("N");
			jobdao.insert(jobEntity);
			jobdao.updateState(jobEntity);
			dbBackupScheme.setJobId(jobEntity.getId());
			try {
				jobService.addJob(jobEntity);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(Exceptions.getStackTraceAsString(e));
			}
		}
	}
	
}