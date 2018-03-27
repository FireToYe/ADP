/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.db.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.exception.CommonException;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.db.entity.DbBackupScheme;
import com.zhilink.srm.manager.modules.db.service.BackupService;
import com.zhilink.srm.manager.modules.db.service.DbBackupSchemeService;

import freemarker.template.SimpleDate;

/**
 * 数据库备份方案Controller
 * @author ben
 * @version 2017-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/db/dbBackupScheme")
public class DbBackupSchemeController extends BaseController {

	@Autowired
	private DbBackupSchemeService dbBackupSchemeService;
	
	@Autowired 
	private BackupService backupService;
	
	@ModelAttribute
	public DbBackupScheme get(@RequestParam(required=false) String id) {
		DbBackupScheme entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dbBackupSchemeService.get(id);
		}
		if (entity == null){
			entity = new DbBackupScheme();
		}
		return entity;
	}
	
	@RequiresPermissions("db:dbBackupScheme:view")
	@RequestMapping(value = {"list", ""})
	public String list(DbBackupScheme dbBackupScheme, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DbBackupScheme> page = dbBackupSchemeService.findPage(new Page<DbBackupScheme>(request, response), dbBackupScheme); 
		model.addAttribute("page", page);
		return "modules/db/dbBackupSchemeList";
	}

	@RequiresPermissions("db:dbBackupScheme:view")
	@RequestMapping(value = "form")
	public String form(DbBackupScheme dbBackupScheme, Model model) {
		model.addAttribute("dbBackupScheme", dbBackupScheme);
		return "modules/db/dbBackupSchemeForm";
	}

	@RequiresPermissions("db:dbBackupScheme:edit")
	@RequestMapping(value = "save")
	public String save(DbBackupScheme dbBackupScheme, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dbBackupScheme)){
			return form(dbBackupScheme, model);
		}
		dbBackupSchemeService.save(dbBackupScheme);
		addMessage(redirectAttributes, I18n.i18nMessage("adb_db.save_scheme"));
		return "redirect:"+Global.getAdminPath()+"/db/dbBackupScheme/?repage";
	}
	
	@RequiresPermissions("db:dbBackupScheme:edit")
	@RequestMapping(value = "delete")
	public String delete(DbBackupScheme dbBackupScheme, RedirectAttributes redirectAttributes) {
		dbBackupSchemeService.delete(dbBackupScheme);
		addMessage(redirectAttributes, I18n.i18nMessage("adb_db.delete_scheme"));
		return "redirect:"+Global.getAdminPath()+"/db/dbBackupScheme/?repage";
	}
	/**
	 * 方案名称校验唯一性
	 * @param dbBackupScheme
	 * @param model
	 * @return
	 */
	@RequiresPermissions("db:dbBackupScheme:view")
	@RequestMapping(value = "checkOnly")
	@ResponseBody
	public String checkOnly(DbBackupScheme dbBackupScheme, Model model) {
		if(dbBackupScheme!=null&&dbBackupSchemeService.checkOnly(dbBackupScheme)==null){
			return "true";
		}
		return "false";
	}
	/*
	 * @param 文件名
	 * @return
	 */
	@RequestMapping(value = "/backupRecord")
	@ResponseBody
	public List<String> getBackupRecord() {
      List<String> backupRecord;
      try{
   	  backupRecord = backupService.getBackupRecord();
   	   
      }catch (Exception e) {
		e.printStackTrace();
		throw new CommonException(e.getMessage());
      }
      return backupRecord;
	}
	/**
	 * 查询所有的表
	 */
	@RequestMapping(value = "stTabeles")
	@ResponseBody
	public List<String> listTables(String fileName) {
		List<String> tables = null;
      try{
   	   tables = backupService.getTables();
   	   
      }catch (Exception e) {
		e.printStackTrace();
		throw new CommonException(e.getMessage());
      }
	return tables;
	}
	/**
	 * 执行备份
	 */
	@RequestMapping(value = "doBackUpTables")
	@ResponseBody
	public String doBackUpTables(DbBackupScheme dbBackupScheme) {
		if(dbBackupScheme==null){
			throw new CommonException(I18n.i18nMessage("adb_db.not_exist_scheme"));
		}
		String tables = dbBackupScheme.getBackupTables();
		if(StringUtils.isEmpty(tables)){
			throw new CommonException(I18n.i18nMessage("adb_db.scheme_not_table"));
		}
		String[] tablesArray = tables.split(",");
      try{
          backupService.doBackUpTables(dbBackupScheme.getSchemeName()+"_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()),tablesArray);
   	   
      }catch (Exception e) {
		e.printStackTrace();
		throw new CommonException(e.getMessage());
      }
	return I18n.i18nMessage("adp_db.success ");
	}
	/**
	 * 还原备份文件
	 */
	@RequestMapping(value = "/restore")
	@ResponseBody
	public String doRestore(@RequestParam String fileName) {
      try{
   	  backupService.doRestore(fileName);
   	   
      }catch (Exception e) {
		e.printStackTrace();
		throw new CommonException(e.getMessage());
      }
      return I18n.i18nMessage("adp_db.success ");
	}
	/**
	 * 删除备份文件
	 */
	@RequestMapping(value = "/deleteFile")
	@ResponseBody
	public String deleteFile(@RequestParam String fileName) {
      try{
   	 	File file = new File(fileName);
   	 	if(file.exists()){
   	 		file.delete();
   	 	}
   	   
      }catch (Exception e) {
		e.printStackTrace();
		throw new CommonException(e.getMessage());
      }
      return "success";
	}

	@RequiresPermissions("db:dbBackupScheme:view")
	@RequestMapping(value ="history")
	public String listHistory(DbBackupScheme dbBackupScheme, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<String> list = backupService.getBackupRecord();
		Page<String> page = new Page<String>(request, response);
		page.setCount(list==null?0:list.size());
		if(list.size()<page.getPageNo()*page.getPageSize()){
			page.setList(list.subList((page.getPageNo()-1)*page.getPageSize(),list.size()));
		}else{
			page.setList(list.subList((page.getPageNo()-1)*page.getPageSize(), page.getPageNo()*page.getPageSize()));
		}
		model.addAttribute("page", page);
		return "modules/db/dbBackupSchemeHistory";
	}
	
}