/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.web;

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

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;
import com.zhilink.srm.manager.modules.sys.service.SysMessageSendService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 消息发送Controller
 * @author zwhua
 * @version 2017-11-06
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysMessageSend")
public class SysMessageSendController extends BaseController {

	@Autowired
	private SysMessageSendService sysMessageSendService;
	
	@ModelAttribute
	public SysMessageSend get(@RequestParam(required=false) String id) {
		SysMessageSend entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysMessageSendService.get(id);
		}
		if (entity == null){
			entity = new SysMessageSend();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:sysMessageSend:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysMessageSend sysMessageSend, HttpServletRequest request, HttpServletResponse response, Model model) {
		sysMessageSend.setCreateBy(UserUtils.getUser());
		Page<SysMessageSend> page = sysMessageSendService.findPage(new Page<SysMessageSend>(request, response), sysMessageSend); 
		model.addAttribute("page", page);
		return "modules/sys/sysMessageSendList";
	}

	@RequestMapping(value = "form")
	public String form(SysMessageSend sysMessageSend, Model model) {
		model.addAttribute("sysMessageSend", sysMessageSend);
		if(StringUtils.isNotBlank(sysMessageSend.getId())){
			return "modules/sys/sysMessageSendViewForm";
		}else{
			return "modules/sys/sysMessageSendForm";
		}
	}

	@RequestMapping(value = "save")
	public String save(SysMessageSend sysMessageSend, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysMessageSend)){
			return form(sysMessageSend, model);
		}
		//附件长度最大1000
		String attachments=sysMessageSend.getAttachmentIds();
		if(attachments.length() > 2000){
			addMessage(redirectAttributes, I18n.i18nMessage("sysMessageSend.nameTooLong"));
			sysMessageSend.setAttachmentIds("");
			sysMessageSend.setAttachmentNames("");
			return "redirect:"+Global.getAdminPath()+"/sys/sysMessageSend/form?repage";
		}
		String receiver = sysMessageSend.getReceivers();

		sysMessageSend.setType("0");
		sysMessageSend.setReceivers(receiver);
		sysMessageSend.setIsNewRecord(true);
		boolean sendedStatus = sysMessageSendService.sendMessage(sysMessageSend);
		if(!sendedStatus){
			addMessage(redirectAttributes, I18n.i18nMessage("sysMessageSend.failSendTip"));
		}else{
			addMessage(redirectAttributes, I18n.i18nMessage("sysMessageSend.sendTip"));
		}
		return "redirect:"+Global.getAdminPath()+"/sys/sysMessageSend/?repage";
	}
	
	@RequestMapping(value = "delete")
	public String delete(SysMessageSend sysMessageSend, RedirectAttributes redirectAttributes) {
		sysMessageSendService.delete(sysMessageSend);
		addMessage(redirectAttributes, I18n.i18nMessage("sysMessageSend.deleteTip"));
		return "redirect:"+Global.getAdminPath()+"/sys/sysMessageSend/?repage";
	}

}