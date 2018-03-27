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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.IdGen;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.manager.modules.sys.entity.PrintTemplate;
import com.zhilink.srm.manager.modules.sys.service.PrintTemplateService;

/**
 * 打印模板Controller
 * @author sushengshun
 * @version 2017-10-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/printTemplate")
public class PrintTemplateController extends BaseController {

	@Autowired
	private PrintTemplateService printTemplateService;
	
	@ModelAttribute
	public PrintTemplate get(@RequestParam(required=false) String id) {
		PrintTemplate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = printTemplateService.get(id);
		}
		if (entity == null){
			entity = new PrintTemplate();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:printTemplate:view")
	@RequestMapping(value = {"list", ""})
	public String list(PrintTemplate printTemplate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PrintTemplate> page = printTemplateService.findPage(new Page<PrintTemplate>(request, response), printTemplate);
		model.addAttribute("printTemplate", printTemplate);
		model.addAttribute("page", page);
		return "modules/sys/printTemplateList";
	}

	@RequiresPermissions("sys:printTemplate:view")
	@RequestMapping(value = "form")
	public String form(PrintTemplate printTemplate, Model model) {
		if(StringUtils.isNotBlank(printTemplate.getId())) {
			printTemplate = printTemplateService.get(printTemplate);
			printTemplate.setTemplateContent(StringEscapeUtils.unescapeHtml4(printTemplate.getTemplateContent()));
			printTemplate.setTemplateParams(StringEscapeUtils.unescapeHtml4(printTemplate.getTemplateParams()));
		}
		model.addAttribute("printTemplate", printTemplate);
		return "modules/sys/printTemplateForm";
	}

	@RequiresPermissions("sys:printTemplate:edit")
	@RequestMapping(value = "save")
	public String save(PrintTemplate printTemplate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, printTemplate)){
			return form(printTemplate, model);
		}
		printTemplateService.save(printTemplate);
		String i18nTip = I18n.i18nMessage("printTemplate.saveSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/printTemplate/?repage";
	}
	
	@RequiresPermissions("sys:printTemplate:edit")
	@RequestMapping(value = "delete")
	public String delete(PrintTemplate printTemplate, RedirectAttributes redirectAttributes) {
		printTemplateService.delete(printTemplate);
		String i18nTip = I18n.i18nMessage("printTemplate.deleteSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/printTemplate/?repage";
	}


	/**
	 * 打印预览，生成预览页面
	 * @param response
	 * @param key
	 */
	@RequiresPermissions("sys:printTemplate:edit")
	@RequestMapping(value = "printShow")
	public void printShow(HttpServletResponse response, String key) {
		try {
			if(StringUtils.isBlank(key)) {
				return;
			}
			String cacheValue =(String) CacheUtils.get(key);
			CacheUtils.remove(key);
			if(StringUtils.isBlank(cacheValue)) {
				return ;
			}
			JSONObject jsonObject = JSON.parseObject(cacheValue);
			String templateContent = (String)jsonObject.get("templateContent");
			String templateParams = jsonObject.getString("templateParams");
			printTemplateService.printShow(response,templateParams, templateContent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 预览前存好参数,ajax无法接受预览的pdf格式
	 * @param templateParams
	 * @param templateContent
	 * @return
	 */
	@RequiresPermissions("sys:printTemplate:edit")
	@RequestMapping(value = "printShowPre")
	@ResponseBody
	public ResultModel printShowPre(String templateParams , String templateContent) {
		ResultModel resultModel = new ResultModel();
		if(StringUtils.isBlank(templateParams)) {
			String i18nTip = I18n.i18nMessage("printTemplate.emptyParameters");
			resultModel.setErrorMsg(i18nTip);
			return resultModel;
		}
		if(StringUtils.isBlank(templateContent)) {
			String i18nTip = I18n.i18nMessage("printTemplate.emptyTemplate");
			resultModel.setErrorMsg(i18nTip);
			return resultModel;
		}
		try {
			templateContent = StringEscapeUtils.unescapeHtml4(templateContent);
			templateParams = StringEscapeUtils.unescapeHtml4(templateParams);
			JSONObject jsonObject = JSONObject.parseObject(templateParams);
			
			JSONObject resultJson = new JSONObject();
			resultJson.put("templateParams", jsonObject.toJSONString());
			resultJson.put("templateContent", templateContent);
			String key="pt"+IdGen.uuid();
			
			CacheUtils.put(key, resultJson.toJSONString());
			resultModel.setBody(key);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			String i18nTip = I18n.i18nMessage("printTemplate.abnormality");
			resultModel.setErrorMsg(i18nTip);
			return resultModel;
		}
		return resultModel;
	}
	/**
	 * 预览html
	 * @param templateParams
	 * @param templateContent
	 * @return
	 */
	@RequiresPermissions("sys:printTemplate:edit")
	@RequestMapping(value = "printHtml")
	@ResponseBody
	public ResultModel printHtml(String templateParams,String templateContent,String number) {
		ResultModel resultModel = new ResultModel();
		if(StringUtils.isBlank(templateParams)) {
			String i18nTip = I18n.i18nMessage("printTemplate.emptyParameters");
			resultModel.setErrorMsg(i18nTip);
			return resultModel;
		}
		if(StringUtils.isBlank(templateContent)) {
			String i18nTip = I18n.i18nMessage("printTemplate.emptyTemplate");
			resultModel.setErrorMsg(i18nTip);
			return resultModel;
		}
		try {
			templateContent = StringEscapeUtils.unescapeHtml4(templateContent);
			templateParams = StringEscapeUtils.unescapeHtml4(templateParams);
			printTemplateService.getHtml(templateParams, templateContent,Integer.parseInt(number));	
		} catch (Exception e) {
			e.printStackTrace();
			String i18nTip = I18n.i18nMessage("printTemplate.abnormality");
			resultModel.setErrorMsg(i18nTip);
			return resultModel;
		}
		return resultModel;
	}
	
}