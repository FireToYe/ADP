package com.zhilink.srm.manager.modules.db.web.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhilink.manager.common.utils.Exceptions;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.db.entity.DbBackupScheme;
import com.zhilink.srm.manager.modules.db.service.BackupService;
import com.zhilink.srm.manager.modules.db.service.DbBackupSchemeService;

@Controller
@RequestMapping(value = "${apiPath}/sys/db")
public class BackupController extends BaseController {
	
	@Autowired
	private DbBackupSchemeService dbBackupSchemeService;
	
	@Autowired 
	private BackupService backupService;
	
	/**
	 * 执行数据备份
	 * @param id 备份方案id
	 * @return
	 */
	@RequestMapping(value = "/backup")
	@ResponseBody
	public ResultModel doBackup(String schemeName) {
		ResultModel result = new ResultModel();		
		if (StringUtils.isNotBlank(schemeName)){
			result.setErrorCode("-1");
			result.setErrorMsg("方案名称不能为空!");
			result.setStatus(0);
			return result;
		}
		DbBackupScheme dbBackupScheme = new DbBackupScheme();
		dbBackupScheme.setSchemeName(schemeName);
		DbBackupScheme entity = dbBackupSchemeService.checkOnly(dbBackupScheme);
		if(entity==null){
			result.setErrorCode("-1");
			result.setErrorMsg("找不到备份方案!");
			result.setStatus(0);
			return result; 
		}
		
		//异步调用数据库备份
		new Thread(){
            public void run() {
            	backupService.doBackUpTables(entity.getBackupTables().split(","));
            }
        }.start();
		
		
		return result;
	}
	
	public String doBackUpByJob(String schemeName){
		if (!StringUtils.isNotBlank(schemeName)){
			logger.error("方案名称为空，执行失败");
			return "方案名称为空，执行失败";
		}
		DbBackupScheme dbBackupScheme = new DbBackupScheme();
		dbBackupScheme.setSchemeName(schemeName);
		DbBackupScheme entity = dbBackupSchemeService.checkOnly(dbBackupScheme);
		if(entity==null){
			logger.error("方案记录不存在，执行失败");
			return "方案记录不存在，执行失败";
		}
		
		//异步调用数据库备份
		backupService.doBackUpTables(schemeName+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),entity.getBackupTables().split(","));

		if(!StringUtils.isEmpty(entity.getSaveCount())){
			try{
				int count = Integer.valueOf(entity.getSaveCount());
				backupService.deleteByName(schemeName, count);
			}catch(Exception e){
				logger.error(Exceptions.getStackTraceAsString(e));
			}
		}
		
		return "执行成功";
	}

}
