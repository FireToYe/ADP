/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.SysMessage;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;
import com.zhilink.srm.manager.modules.sys.service.SysMessageService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 消息提醒Controller
 * @author zwhua
 * @version 2017-11-03
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysMessage")
public class SysMessageController extends BaseController {

	@Autowired
	private SysMessageService sysMessageService;
	
	@ModelAttribute
	public SysMessage get(@RequestParam(required=false) String id) {
		SysMessage entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysMessageService.get(id);
		}
		if (entity == null){
			entity = new SysMessage();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:SysMessageSend:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysMessage sysMessage, HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(required=false) String dataArray
			) {
		if(!StringUtils.isNotBlank(sysMessage.getAccepterId()) ||  sysMessage.getAccepterId()==null || "".equals(sysMessage.getAccepterId())){
			sysMessage.setAccepterId(UserUtils.getUser().getId());
		}
		model.addAttribute("dataArray", dataArray);
		Page<SysMessage> page = sysMessageService.findPage(new Page<SysMessage>(request, response), sysMessage); 
		model.addAttribute("page", page);
		return "modules/sys/sysMessageList";
	}

	//管理员查看已经发送的消息被查看的情况
	@RequestMapping(value = "statusList")
	public String statusList(SysMessage sysMessage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysMessage> page = sysMessageService.findStatusList(new Page<SysMessage>(request, response), sysMessage);
		model.addAttribute("page", page);
		return "modules/sys/sysMessageStatusList";
	}
	
	//管理员删除已经发送的消息
	@RequestMapping(value = "StatusDelete")
	public String StatusDelete(SysMessage sysMessage, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		sysMessageService.delete(sysMessage);
		String i18nTip = I18n.i18nMessage("sysMessage.deleteSuccessful");
		addMessage(redirectAttributes, i18nTip);
		redirectAttributes.addAttribute("sourceMessageId",request.getParameter("sourceMessageId"));
		redirectAttributes.addAttribute("officeName",request.getParameter("officeName"));
		redirectAttributes.addAttribute("companyName",request.getParameter("companyName"));
		redirectAttributes.addAttribute("receiverName",request.getParameter("receiverName"));
		redirectAttributes.addAttribute("delStatus",request.getParameter("delStatus"));
		return "redirect:"+Global.getAdminPath()+"/sys/sysMessage/statusList?repage";
	}
	
	//获取当前用户的未读消息数量
	@ResponseBody
	@RequestMapping(value = "notifyNum")
	public int notifyNum(String delStatus){
		String userId = UserUtils.getUser().getId();
		int num = sysMessageService.notifyNum(userId,delStatus);
		return num;
	}
	
	//点击查看消息时，更新消息状态
	@RequestMapping(value = "delStatus")
	public String delStatus(SysMessage sysMessage, Model model,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		sysMessage.setDelStatus("1");
		sysMessage.setAccepterId(UserUtils.getUser().getId());
		sysMessageService.updateStatus(sysMessage);
	
		redirectAttributes.addAttribute("title",request.getParameter("title"));
		redirectAttributes.addAttribute("delStatus",request.getParameter("delStatus"));
		return "redirect:"+Global.getAdminPath()+"/sys/sysMessage/?repage";
	}

	@RequiresPermissions("sys:SysMessageSend:view")
	@RequestMapping(value = "form")
	public String form(SysMessage sysMessage, Model model) {
		if(!"1".equals(sysMessage.getDelStatus())){
			sysMessage.setDelStatus("1");
			sysMessageService.save(sysMessage);
		}
		model.addAttribute("sysMessage", sysMessage);
		return "modules/sys/sysMessageForm";
	}
	
	@RequiresPermissions("sys:SysMessageSend:edit")
	@RequestMapping(value = "save")
	public String save(SysMessage sysMessage, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysMessage)){
			return form(sysMessage, model);
		}
		sysMessageService.save(sysMessage);
		String i18nTip = I18n.i18nMessage("sysMessage.saveSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/sysMessage/?repage";
	}
	
	@RequiresPermissions("sys:SysMessageSend:edit")
	@RequestMapping(value = "delete")
	public String delete(SysMessage sysMessage, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		sysMessageService.delete(sysMessage);
		String i18nTip = I18n.i18nMessage("sysMessage.deleteSuccessful");
		addMessage(redirectAttributes, i18nTip);
		
		redirectAttributes.addAttribute("title",request.getParameter("title"));
		redirectAttributes.addAttribute("delStatus",request.getParameter("delStatus"));
		return "redirect:"+Global.getAdminPath()+"/sys/sysMessage/?repage&";
	}
	@RequestMapping(value = "batchDelete")
	public String batchDelete(SysMessage sysMessage, RedirectAttributes redirectAttributes, @RequestParam(required=false) String dataArray,HttpServletRequest request) {
		String[] ids = dataArray.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").trim().split(",");
		
		redirectAttributes.addAttribute("title",request.getParameter("title"));
		redirectAttributes.addAttribute("delStatus",request.getParameter("delStatus"));
		sysMessageService.batchDelete(ids);
		String i18nTip = I18n.i18nMessage("sysMessage.deleteSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/sysMessage/?repage";
	}

}