package com.zhilink.srm.manager.modules.sys.web;

import java.util.List;

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

import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.manager.modules.sys.entity.SysExtendWord;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;
import com.zhilink.srm.manager.modules.sys.service.SysExtendWordService;

/**
 * 扩展字段Controller
 * @version 2017-12-20
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/extendWord")
public class SysExtendWordController extends BaseController {

	@Autowired
	private SysExtendWordService sysExtendWordService;
	
	@ModelAttribute
	public SysExtendWord get(@RequestParam(required=false) String id) {
		SysExtendWord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysExtendWordService.get(id);
		}
		if (entity == null){
			entity = new SysExtendWord();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:sysExtendWord:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysExtendWord sysExtendWord, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SysExtendWord> keyList = sysExtendWordService.findKeyList();
		model.addAttribute("keyList", keyList);
		Page<SysExtendWord> page = sysExtendWordService.findPage(new Page<SysExtendWord>(request, response), sysExtendWord); 
		model.addAttribute("page", page);
		return "modules/sys/sysExtendWordList";
	}
	
	@RequiresPermissions("sys:sysExtendWord:view")
	@RequestMapping(value = "form")
	public String form(SysExtendWord sysExtendWord, Model model) {
		model.addAttribute("sysExtendWord", sysExtendWord);
		if(StringUtils.isNotBlank(sysExtendWord.getId())){
			return "modules/sys/sysExtendWordForm";
		}else{
			return "modules/sys/sysExtendWordForm";
		}
	}
	
	@RequiresPermissions("sys:sysExtendWord:edit")
	@RequestMapping(value = "save")
	public String save(SysExtendWord sysExtendWord, Model model, RedirectAttributes redirectAttributes){
		if (!beanValidator(model, sysExtendWord)){
			return form(sysExtendWord, model);
		}
		sysExtendWordService.save(sysExtendWord);
		addMessage(redirectAttributes, "保存成功");
		
		return "redirect:"+Global.getAdminPath()+"/sys/extendWord/?repage";
	}
	
	@RequiresPermissions("sys:sysExtendWord:edit")
	@RequestMapping(value = "delete")
	public String delete(SysExtendWord sysExtendWord, Model model, RedirectAttributes redirectAttributes){
		if (!beanValidator(model, sysExtendWord)){
			return form(sysExtendWord, model);
		}
		sysExtendWordService.delete(sysExtendWord);
		addMessage(redirectAttributes, "删除成功");
		
		return "redirect:"+Global.getAdminPath()+"/sys/extendWord/?repage";
	}
	
	/**
	 * 判断是否存在相同的key和name
	 * @param key,name
	 */
	@RequestMapping(value = "existSameData")
	@ResponseBody
	public int existSameData(@RequestParam(required = true) String key,@RequestParam(required = true) String name,@RequestParam(required = true) String id){
		int num = sysExtendWordService.existSameData(key, name,id);
		return num;
	}
	
}