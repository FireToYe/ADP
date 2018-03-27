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
import com.zhilink.srm.manager.modules.sys.entity.News;
import com.zhilink.srm.manager.modules.sys.service.NewsService;

/**
 * 新闻通告Controller
 * @author an48huf
 * @version 2017-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/news")
public class NewsController extends BaseController {

	@Autowired
	private NewsService newsService;
	
	@ModelAttribute
	public News get(@RequestParam(required=false) String id) {
		News entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = newsService.get(id);
		}
		if (entity == null){
			entity = new News();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:news:view")
	@RequestMapping(value = {"list", ""})
	public String list(News news, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<News> page = newsService.findPage(new Page<News>(request, response), news); 
		model.addAttribute("page", page);
		return "modules/sys/newsList";
	}

	@RequiresPermissions("sys:news:view")
	@RequestMapping(value = "form")
	public String form(News news, Model model) {
		//新增默认选中显示
		if(news.getIsNewRecord()) {
			news.setPublish("1");
		}
		model.addAttribute("news", news);
		return "modules/sys/newsForm";
	}

	@RequiresPermissions("sys:news:edit")
	@RequestMapping(value = "save")
	public String save(News news, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, news)){
			return form(news, model);
		}
		newsService.save(news);
		String i18nTip = I18n.i18nMessage("news.saveSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/news/?repage";
	}
	
	@RequiresPermissions("sys:news:edit")
	@RequestMapping(value = "delete")
	public String delete(News news, RedirectAttributes redirectAttributes) {
		newsService.delete(news);
		String i18nTip = I18n.i18nMessage("news.deleteSuccessful");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/news/?repage";
	}

}