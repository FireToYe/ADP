/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.utils.JdbcUtils;
import com.alibaba.fastjson.JSON;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.SysAppSql;
import com.zhilink.srm.manager.modules.sys.service.SysAppSqlService;

/**
 * 应用SQL管理Controller
 * @author xh
 * @version 2017-12-12
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysAppSql")
public class SysAppSqlController extends BaseController {

	@Autowired
	private SysAppSqlService sysAppSqlService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@ModelAttribute
	public SysAppSql get(@RequestParam(required=false) String id) {
		SysAppSql entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysAppSqlService.get(id);
		}
		if (entity == null){
			entity = new SysAppSql();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:sysAppSql:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysAppSql sysAppSql, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysAppSql> page = sysAppSqlService.findPage(new Page<SysAppSql>(request, response), sysAppSql); 
		model.addAttribute("page", page);
		return "modules/sys/sysAppSqlList";
	}

	@RequiresPermissions("sys:sysAppSql:view")
	@RequestMapping(value = "form")
	public String form(SysAppSql sysAppSql, Model model) {
		model.addAttribute("sysAppSql", sysAppSql);
		return "modules/sys/sysAppSqlForm";
	}

	@RequiresPermissions("sys:sysAppSql:edit")
	@RequestMapping(value = "save")
	public String save(SysAppSql sysAppSql, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysAppSql)){
			return form(sysAppSql, model);
		}
		sysAppSql.setSqltext(StringEscapeUtils.unescapeHtml4(sysAppSql.getSqltext()));
		sysAppSql.setRollbacktext(StringEscapeUtils.unescapeHtml4(sysAppSql.getRollbacktext()));
		sysAppSqlService.save(sysAppSql);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysAppSql/?repage";
	}
	
	@RequiresPermissions("sys:sysAppSql:edit")
	@RequestMapping(value = "updateErrorSql")
	public void updateErrorSql(@RequestParam(required=false) String id) {		
		sysAppSqlService.updateErrorSql(id);
	}
	
	@RequiresPermissions("sys:sysAppSql:edit")
	@RequestMapping(value = "delete")
	public String delete(SysAppSql sysAppSql, RedirectAttributes redirectAttributes) {
		sysAppSqlService.delete(sysAppSql);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/sys/sysAppSql/?repage";
	}

	@RequiresPermissions("sys:sysAppSql:edit")
	@RequestMapping(value = "executeSql")
	@ResponseBody
		public String executeSql(@RequestParam(required=false) String sql) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			boolean result=JdbcUtils.executeBatchSqls(jdbcTemplate, sql);
			if(result){
				resultMap.put("msg","执行sql脚本成功");
			}else {
				resultMap.put("msg","执行sql脚本失败");
			}
		}catch (Exception e) {
			e.printStackTrace();
			resultMap.put("msg","发生异常，联系管理员");
		}
		return JSON.toJSONString(resultMap);
	}
}