/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.Collections3;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Office;
import com.zhilink.srm.manager.modules.sys.entity.Role;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.service.OfficeService;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 角色Controller
 * @author jaray
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		List<Role> list = systemService.findAllRole();
		model.addAttribute("list", list);
		return "modules/sys/roleList";
	}

	/**
	 * 发送消息时要选择的角色
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "messageRoleList")
	public String messageRoleList(Role role, Model model) {
		List<Role> list = systemService.findAllRole();
		model.addAttribute("list", list);
		return "modules/sys/messageRoleList";
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		if (role.getOffice()==null){
			role.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("role", role);
		model.addAttribute("menuList", systemService.findAllMenu());
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/roleForm";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, Model model, RedirectAttributes redirectAttributes) {
		String i18nTip = "";
		if(!UserUtils.getUser().isAdmin()&&role.getSysData().equals(Global.YES)){
			i18nTip = I18n.i18nMessage("role.overPowerTip");
			addMessage(redirectAttributes, i18nTip);
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if (!beanValidator(model, role)){
			return form(role, model);
		}
		if (!"true".equals(checkName(role.getOldName(), role.getName()))){
			i18nTip = I18n.i18nMessage("role.saveRoleFailer");
			String[] params = {role.getName()};
			String content = MessageFormat.format(i18nTip, params);
			addMessage(model, content);
			return form(role, model);
		}
		if (!"true".equals(checkEnname(role.getOldEnname(), role.getEnname()))){
			i18nTip = I18n.i18nMessage("role.saveRoleFailers");
			String[] params = {role.getName()};
			String content = MessageFormat.format(i18nTip, params);
			addMessage(model, content);
			return form(role, model);
		}
		systemService.saveRole(role);
		i18nTip = I18n.i18nMessage("role.saveRoleSuccessful");
		String[] params = {role.getName()};
		String content = MessageFormat.format(i18nTip, params);
		addMessage(redirectAttributes, content);
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(Role role, RedirectAttributes redirectAttributes) {
		String i18nTip = "";
		if(!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)){
			i18nTip = I18n.i18nMessage("role.overPowerTip");
			addMessage(redirectAttributes, i18nTip);
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
//		if (Role.isAdmin(id)){
//			addMessage(redirectAttributes, "删除角色失败, 不允许内置角色或编号空");
////		}else if (UserUtils.getUser().getRoleIdList().contains(id)){
////			addMessage(redirectAttributes, "删除角色失败, 不能删除当前用户所在角色");
//		}else{
			systemService.deleteRole(role);
			i18nTip = I18n.i18nMessage("role.deleteRoleSuccessful");
			addMessage(redirectAttributes, i18nTip);
//		}
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	/**
	 * 角色分配页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		List<User> userList = systemService.findUser(new User(new Role(role.getId())));
		model.addAttribute("userList", userList);
		return "modules/sys/roleAssign";
	}
	
	/**
	 * 角色分配 -- 打开角色分配对话框
	 * @param role
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		List<User> userList = systemService.findUser(new User(new Role(role.getId())));
		model.addAttribute("role", role);
		model.addAttribute("userList", null);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/selectUserToRole";
	}
	
	/**
	 * 角色分配 -- 根据部门编号获取用户列表
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user = new User();
		user.setOffice(new Office(officeId));
		Page<User> page = systemService.findUser(new Page<User>(1, -1), user);
		for (User e : page.getList()) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	/**
	 * 角色分配 -- 从角色中移除用户
	 * @param userId
	 * @param roleId
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "outrole")
	public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
		String i18nTip = "";
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/assign?id="+roleId;
		}
		Role role = systemService.getRole(roleId);
		User user = systemService.getUser(userId);
		if (UserUtils.getUser().getId().equals(userId)) {
			i18nTip = I18n.i18nMessage("role.removeItselfTip");
			String[] params = {role.getName(),user.getName()};
			String content = MessageFormat.format(i18nTip, params);
			addMessage(redirectAttributes, content);
		}else {
			if (user.getRoleList().size() <= 1){
				i18nTip = I18n.i18nMessage("role.removeLastRoleTip");
				String[] params = {role.getName(),user.getName()};
				String content = MessageFormat.format(i18nTip, params);
				addMessage(redirectAttributes, content);
			}else{
				Boolean flag = systemService.outUserInRole(role, user);
				if (!flag) {
					i18nTip = I18n.i18nMessage("role.removeFailerTip");
					String[] params = {role.getName(),user.getName()};
					String content = MessageFormat.format(i18nTip, params);
					addMessage(redirectAttributes, content);
				}else {
					i18nTip = I18n.i18nMessage("role.removeSuccessfulTip");
					String[] params = {role.getName(),user.getName()};
					String content = MessageFormat.format(i18nTip, params);
					addMessage(redirectAttributes, content);
				}
			}		
		}
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}
	
	/**
	 * 角色分配
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(Role role,String idsArr, RedirectAttributes redirectAttributes) {
		String i18nTip = "";
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
		}
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		String[] idsArray=idsArr.split(",");
		for (int i = 0; i < idsArray.length; i++) {
			User user = systemService.assignUserToRole(role, systemService.getUser(idsArray[i]));
			if (null != user) {
				i18nTip = I18n.i18nMessage("role.addUserToRole");
				String[] params = {user.getName(),role.getName()};
				String content = MessageFormat.format(i18nTip, params);
				msg.append("<br/>"+content);
				newNum++;
			}
		}
		i18nTip = I18n.i18nMessage("role.allocatedTip");
		Object[] params = {newNum,msg};
		String content = MessageFormat.format(i18nTip, params);
		addMessage(redirectAttributes, content);
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}

	/**
	 * 验证角色名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && systemService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 验证角色英文名是否有效
	 * @param oldName
	 * @param name
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, String enname) {
		if (enname!=null && enname.equals(oldEnname)) {
			return "true";
		} else if (enname!=null && systemService.getRoleByEnname(enname) == null) {
			return "true";
		}
		return "false";
	}

}
