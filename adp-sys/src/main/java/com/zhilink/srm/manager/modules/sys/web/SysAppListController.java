/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.manager.modules.sys.entity.SysAppList;
import com.zhilink.srm.manager.modules.sys.service.SysAppListService;

/**
 * app清单表Controller
 * @author chrisye
 * @version 2017-09-28
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysAppList")
public class SysAppListController extends BaseController {

	@Autowired
	private SysAppListService sysAppListService;
	
	@ModelAttribute
	public SysAppList get(@RequestParam(required=false) String id) {
		SysAppList entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysAppListService.get(id);
		}
		if (entity == null){
			entity = new SysAppList();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:sysAppList:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysAppList sysAppList, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysAppList> page = sysAppListService.findPage(new Page<SysAppList>(request, response), sysAppList); 
		model.addAttribute("page", page);
		return "modules/sys/sysAppListList";
	}

	@RequiresPermissions("sys:sysAppList:view")
	@RequestMapping(value = "form")
	public String form(SysAppList sysAppList, Model model) {
		model.addAttribute("sysAppList", sysAppList);
		return "modules/sys/sysAppListForm";
	}

	@RequiresPermissions("sys:sysAppList:edit")
	@RequestMapping(value = "save")
	public String save(SysAppList sysAppList, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysAppList)){
			return form(sysAppList, model);
		}
		sysAppList.setInfoKeys(StringEscapeUtils.unescapeHtml4(sysAppList.getInfoKeys()));
		sysAppListService.save(sysAppList);
		String i18nTip = I18n.i18nMessage("app.saveSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/sysAppList/?repage";
	}
	
	@RequiresPermissions("sys:sysAppList:edit")
	@RequestMapping(value = "delete")
	public String delete(SysAppList sysAppList, RedirectAttributes redirectAttributes) {
		sysAppListService.delete(sysAppList);
		String i18nTip = I18n.i18nMessage("app.deleteSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/sysAppList/?repage";
	}

}