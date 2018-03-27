package com.zhilink.srm.manager.modules.db.test;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhilink.srm.manager.modules.db.service.BackupService;

public class DbTest {
	
	public static void main(String[] args) throws IOException {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring-context-db.xml"});
        context.start();
        BackupService backupService = (BackupService) context.getBean("backupService"); 
        //读取数据表所有表
        //backupService.getTables();
        
        //整库备份
        //backupService.doBackUpAll();
        
        //选表备份
        //backupService.doBackUpTables("testbackupgz" ,new String[]{"sys_user", "sys_dict"});
        
        //读取备份记录
        //backupService.getBackupRecord();
        
        //从zip还源
        //backupService.doRestore("testbackup.zip");
        
        //从 gz还源
        //backupService.doRestore("testbackupgz.gz");
        
		
        //System.in.read(); // press any key to exit
		
	}

}
