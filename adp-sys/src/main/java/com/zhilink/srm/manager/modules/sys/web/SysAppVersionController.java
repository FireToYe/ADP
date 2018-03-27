/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.common.web.ResourceSqlBean;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.manager.modules.sys.entity.SysAppVersion;
import com.zhilink.srm.manager.modules.sys.service.SysAppVersionService;

/**
 * 应用版本管理Controller
 * @author keris
 * @version 2017-10-13
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysAppVersion")
public class SysAppVersionController extends BaseController {
    private final static String PROJECT_NAME = "adp-sys";
    private final static String MENU_SQL ="menu.sql";
    private final static String ORIGINAL_VERSION = "v0.0";
 
	@Autowired
	private SysAppVersionService sysAppVersionService;
	@Autowired
	private ResourceSqlBean resourceSqlBean;
	
	@ModelAttribute
	public SysAppVersion get(@RequestParam(required=false) String id) {
		SysAppVersion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysAppVersionService.get(id);
		}
		if (entity == null){
			entity = new SysAppVersion();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:sysAppVersion:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysAppVersion sysAppVersion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysAppVersion> page = sysAppVersionService.findPage(new Page<SysAppVersion>(request, response), sysAppVersion); 
		model.addAttribute("page", page);
		return "modules/sys/sysAppVersionList";
	}

	@RequiresPermissions("sys:sysAppVersion:view")
	@RequestMapping(value = "form")
	public String form(SysAppVersion sysAppVersion, Model model) {
		
		
		if(StringUtils.isNotEmpty(sysAppVersion.getAppname())){
			String appname=sysAppVersion.getAppname();
			List<String> versionsList =	resourceSqlBean.getAllVersions(appname);
			model.addAttribute("versionsList", versionsList);
		}
			
		model.addAttribute("sysAppVersion", sysAppVersion);
		return "modules/sys/sysAppVersionForm";
	}

	@RequiresPermissions("sys:sysAppVersion:edit")
	@RequestMapping(value = "save")
	public String save(SysAppVersion sysAppVersion, Model model, RedirectAttributes redirectAttributes) {
		String i18nTip ="";
		if (!beanValidator(model, sysAppVersion)){
			return form(sysAppVersion, model);
		}
		try{
		if(StringUtils.isNotEmpty(sysAppVersion.getNewversion())){
			String appname = sysAppVersion.getAppname();
			String version = sysAppVersion.getVersion();
			String newversion = sysAppVersion.getNewversion();
			if(newversion.compareTo(version)>0){
				resourceSqlBean.updateToVersion(appname, version, newversion);
				i18nTip = I18n.i18nMessage("appVersion.saveUpVersionTip");
				addMessage(redirectAttributes, i18nTip);
		}else if(newversion.compareTo(version)<0){
			resourceSqlBean.updateToVersion(appname,newversion);
			i18nTip = I18n.i18nMessage("appVersion.saveUpVersionTip");
			addMessage(redirectAttributes, i18nTip);
		}else{
			i18nTip = I18n.i18nMessage("appVersion.sameVersionTip");
			addMessage(redirectAttributes, i18nTip);
		}
				return "redirect:"+Global.getAdminPath()+"/sys/sysAppVersion/?repage";
			}
		}catch(Exception e){
			e.printStackTrace();
			i18nTip = I18n.i18nMessage("appVersion.executeErrorTip");
			addMessage(redirectAttributes,i18nTip);
		}
		
		sysAppVersionService.save(sysAppVersion);
		i18nTip = I18n.i18nMessage("appVersion.saveVersionTip");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/sysAppVersion/?repage";
	}
	
	@RequiresPermissions("sys:sysAppVersion:edit")
	@RequestMapping(value = "delete")
	public String delete(SysAppVersion sysAppVersion, RedirectAttributes redirectAttributes) {
		String i18nTip = "";
		String appname = sysAppVersion.getAppname();
		if(StringUtils.isEmpty(appname)){
			i18nTip = I18n.i18nMessage("appVersion.parameterDeletion");
			addMessage(redirectAttributes, i18nTip);
		}else{
			sysAppVersion.setAppname(appname);
			sysAppVersionService.delete(sysAppVersion);
			i18nTip = I18n.i18nMessage("appVersion.deleteTip");
			addMessage(redirectAttributes, i18nTip);
		}		
		return "redirect:"+Global.getAdminPath()+"/sys/sysAppVersion/?repage";
	}
	  @RequiresPermissions("sys:sysAppVersion:view")
		@RequestMapping(value = "find")
		@ResponseBody
		public ResultModel findAppname(@RequestParam(required=false) String appname) {
		  
	    	ResultModel resultModel = new ResultModel();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if((appname != null && !"".equals(appname))){
				SysAppVersion sysAppVersion = sysAppVersionService.getAppname(appname);
				if(sysAppVersion == null){
					resultMap.put("result", "error");
				}else{
					resultMap.put("result", "true");
				}
			}else{
				
				}				
				resultModel.setBody(resultMap);
				
				return resultModel;
			}
	  
	@RequiresPermissions("sys:sysAppVersion:edit")
	@RequestMapping(value = "executeMenu")
	@ResponseBody
	public String executeMenu(@RequestParam(required = false) String appname) {
		String i18nTip = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotEmpty(appname)) {
				if (PROJECT_NAME.equals(appname)) {
					if (resourceSqlBean.executeSqlFile(PROJECT_NAME, MENU_SQL)) {
						i18nTip = I18n.i18nMessage("appVersion.executeScriptTip");
						resultMap.put("msg", i18nTip);
					}
				} else {
					if (resourceSqlBean.executeSqlFile(PROJECT_NAME, MENU_SQL)) {
						if ("true".equals(resourceSqlBean.executeSqlFile(appname, MENU_SQL))) {
							i18nTip = I18n.i18nMessage("appVersion.executeScriptTip");
							resultMap.put("msg", i18nTip);
						} else {
							i18nTip = I18n.i18nMessage("appVersion.executeFailedTip");
							resultMap.put("msg", i18nTip);
						}
					}
				}
			} else {
				i18nTip = I18n.i18nMessage("appVersion.executeFailedTip");
				resultMap.put("msg", i18nTip);

			}
		} catch (Exception e) {
			e.printStackTrace();
			i18nTip = I18n.i18nMessage("appVersion.abnormalityTip");
			resultMap.put("msg", i18nTip);
		}
		return JSON.toJSONString(resultMap);
	}

	  @RequiresPermissions("sys:sysAppVersion:edit")
	  @RequestMapping(value = "executeAllSql")
	  @ResponseBody
		public String executeAllsql(@RequestParam(required=false) String appname) {
		  	String i18nTip = "";
			Map<String, Object> resultMap = new HashMap<String, Object>();
			String version= null;
			try{
			if(StringUtils.isNotEmpty(appname)){			
			List<String> versions = resourceSqlBean.getAllVersions(appname);
			if(versions.size()>0){
				version = versions.get(versions.size()-1);
				resourceSqlBean.updateToVersion(appname, ORIGINAL_VERSION, version);
				i18nTip = I18n.i18nMessage("appVersion.executeSqlTip");
				resultMap.put("msg",i18nTip);
			}else{
				i18nTip = I18n.i18nMessage("appVersion.noSqlTip");
				resultMap.put("msg",i18nTip);
			}
			}else{
				i18nTip = I18n.i18nMessage("appVersion.parameterDeletion");
				resultMap.put("msg",i18nTip);
			}
			}catch(Exception e){
				e.printStackTrace();
				i18nTip = I18n.i18nMessage("appVersion.abnormalityTip");
				resultMap.put("msg",i18nTip);
			}
			return JSON.toJSONString(resultMap);
	  }
	  
	  
	  	  
}