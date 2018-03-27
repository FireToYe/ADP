/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

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

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.manager.common.config.PropertiesHolder;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.SysProperties;
import com.zhilink.srm.manager.modules.sys.service.SysPropertiesService;

/**
 * 系统参数Controller
 * @author chrisye
 * @version 2018-01-16
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysProperties")
public class SysPropertiesController extends BaseController {

	@Autowired
	private SysPropertiesService sysPropertiesService;
	@Autowired
	private PropertiesHolder propertiesHolder;
	@ModelAttribute
	public SysProperties get(@RequestParam(required=false) String id) {
		SysProperties entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysPropertiesService.get(id);
		}
		if (entity == null){
			entity = new SysProperties();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:sysProperties:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysProperties sysProperties, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysProperties> page = sysPropertiesService.findPage(new Page<SysProperties>(request, response), sysProperties); 
		model.addAttribute("page", page);
		return "modules/sys/sysPropertiesList";
	}

	@RequiresPermissions("sys:sysProperties:view")
	@RequestMapping(value = "form")
	public String form(SysProperties sysProperties, Model model) {
		model.addAttribute("sysProperties", sysProperties);
		return "modules/sys/sysPropertiesForm";
	}

	@RequiresPermissions("sys:sysProperties:edit")
	@RequestMapping(value = "save")
	public String save(SysProperties sysProperties, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysProperties)){
			return form(sysProperties, model);
		}
		sysPropertiesService.save(sysProperties);
		addMessage(redirectAttributes, "保存系统参数成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysProperties/?repage";
	}
	
	@RequiresPermissions("sys:sysProperties:edit")
	@RequestMapping(value = "delete")
	public String delete(SysProperties sysProperties, RedirectAttributes redirectAttributes) {
		sysPropertiesService.delete(sysProperties);
		addMessage(redirectAttributes, "删除系统参数成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysProperties/?repage";
	}

	@RequiresPermissions("sys:sysProperties:view")
	@RequestMapping(value = "systemList")
	public String systemList( Model model) {
		Set<Entry<Object, Object>> set = propertiesHolder.getProperties().entrySet();
		Set<Entry<Object, Object>> proSet = new HashSet<Entry<Object, Object>>();
		/*set.removeIf(new Predicate<Entry<Object, Object>>() {

			@Override
			public boolean test(Entry<Object, Object> t) {
				// TODO Auto-generated method stub
				if(t.getKey()!=null){
					if(t.getKey() instanceof String){
						if(((String) t.getKey()).contains("password"))
						return true;
					}
				}
				return false;
			}
			
		});*/
		for(Entry<Object, Object> t:set){
			Object key = t.getKey();
			if(key instanceof String){
				String keyStr = (String)key;
				if(!keyStr.contains("jdbc")&&!keyStr.contains("password")){
					proSet.add(t);
				}
			}
		}
		model.addAttribute("propertiesSet",proSet);
		return "modules/sys/systemPropertiesList";
	}
	

	@RequiresPermissions("sys:sysProperties:view")
	@RequestMapping(value = "checkOnly")
	@ResponseBody
	public String checkOnly(SysProperties sysProperties, Model model) {
		if(sysPropertiesService.checkOnly(sysProperties)==null){
			return "true";
		}
		return "false";
	}
	
}