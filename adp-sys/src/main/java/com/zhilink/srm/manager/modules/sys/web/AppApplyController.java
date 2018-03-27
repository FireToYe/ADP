/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.AppApply;
import com.zhilink.srm.manager.modules.sys.entity.SysAppList;
import com.zhilink.srm.manager.modules.sys.service.AppApplyService;
import com.zhilink.srm.manager.modules.sys.service.SysAppListService;

/**
 * app授权申请表Controller
 * @author chrisye
 * @version 2017-09-28
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/appApply")
public class AppApplyController extends BaseController {

	@Autowired
	private AppApplyService appApplyService;
	
	@Autowired
	private SysAppListService sysAppListService;
	
	//@Autowired
	//private SystemService systemService;
	
	@ModelAttribute
	public AppApply get(@RequestParam(required=false) String openId) {
		AppApply entity = null;
		if (StringUtils.isNotBlank(openId)){
			entity = appApplyService.get(openId);
		}
		if (entity == null){
			entity = new AppApply();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:appApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppApply appApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<AppApply> page = appApplyService.findPage(new Page<AppApply>(request, response), appApply); 
		model.addAttribute("page", page);
		return "modules/sys/appApplyList";
	}

	@RequiresPermissions("sys:appApply:view")
	@RequestMapping(value = "form")
	public String form(AppApply appApply, Model model) {
		Map<String, Object> bodyMap  =new HashMap<String, Object>();
		model.addAttribute("appApply", appApply);
		try{
			//bodyMap = (Map<String, Object>) JSON.parse(appApply.getApplyInfo());
			SysAppList appInfo= sysAppListService.getAppInfo(appApply.getAppId());
			model.addAttribute("appInfo", appInfo);
			JSONObject objKeys= JSONObject.parseObject(appInfo.getInfoKeys());
			JSONObject objApplyInfo= JSONObject.parseObject(appApply.getApplyInfo());
			for(Entry<String, Object> kv : objApplyInfo.entrySet()){
				if(objKeys.containsKey(kv.getKey())){
					bodyMap.put(objKeys.getString(kv.getKey()), kv.getValue());
				}else{
					bodyMap.put(kv.getKey(), kv.getValue());
				}
			}
		}catch(Exception e){
			bodyMap.put("参数",appApply.getApplyInfo());
		}
		model.addAttribute("bodyMap", bodyMap);
		return "modules/sys/appApplyForm";
	}

	@RequiresPermissions("sys:appApply:edit")
	@RequestMapping(value = "save")
	public String save(AppApply appApply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, appApply)){
			return form(appApply, model);
		}
		appApplyService.save(appApply);
		String i18nTip = I18n.i18nMessage("app.saveAuthorizationSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/appApply/?repage";
	}
	
	@RequiresPermissions("sys:appApply:edit")
	@RequestMapping(value = "delete")
	public String delete(AppApply appApply, RedirectAttributes redirectAttributes) {
		appApplyService.delete(appApply);
		String i18nTip = I18n.i18nMessage("app.deleteAutorizationSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/appApply/?repage";
	}
	
	@RequiresPermissions("sys:appApply:edit")
	@RequestMapping(value = "examine")
	public String examine(AppApply appApply, RedirectAttributes redirectAttributes) {
		appApplyService.updateStatus(appApply);
		String i18nTip = I18n.i18nMessage("app.deleteAutorizationCheckSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/appApply/?repage";
	}

	@RequiresPermissions("sys:appApply:edit")
	@RequestMapping(value = "examineInfo")
	public String examineInfo(AppApply appApply, RedirectAttributes redirectAttributes) {
		if("new".equals(appApply.getRelateUser())){
			//添加新用户
			//User user=new User();
			//很多必填信息不会怎么给值，暂时注释
			//systemService.saveUser(user);			
			//appApply.setRelateUser(user.getId());
			appApply.setRelateUser(null);
		}
		
		appApplyService.updateStatus(appApply);
		String i18nTip = I18n.i18nMessage("app.deleteAutorizationCheckSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/appApply/form?openId="+appApply.getOpenId();
	}
}