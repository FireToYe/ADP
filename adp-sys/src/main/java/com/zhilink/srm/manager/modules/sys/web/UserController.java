/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.common.cache.EhCacheUtils;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.beanvalidator.BeanValidators;
import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.utils.excel.ExportExcel;
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Office;
import com.zhilink.srm.manager.modules.sys.entity.Role;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.service.OfficeService;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 用户Controller
 * 
 * @author jaray
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	@Autowired
	private OfficeService officeService;

	@ModelAttribute
	public User get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return systemService.getUser(id);
		} else {
			return new User();
		}
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = { "index" })
	public String index(User user, Model model) {
		return "modules/sys/userIndex";
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = { "list", "" })
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		if (user != null && user.getOffice() != null && user.getOffice().getParent() != null
				&& user.getOffice().getParent().getId() != null && user.getOffice().getParent().getId() != "") {
			if (user.getOffice().getParent().getName() == null || user.getOffice().getParent().getName() == "") {
				Office office = null;
				office = officeService.get(user.getOffice().getParent().getId());
				user.setCompany(office);
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		return "modules/sys/userList";
	}

	// 选择主副负责人
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = { "personList", "" })
	public String personList(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (user.getOffice() == null || user.getOffice().getId() == null || "".equals(user.getOffice().getId())) {
			return "modules/sys/officeChargePersonList";
		}
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		return "modules/sys/officeChargePersonList";
	}

	// 接收消息的人
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "accepter")
	public String accepter(User user, HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(required = false) String dataArray) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);

		dataArray = StringEscapeUtils.unescapeHtml4(dataArray);
		model.addAttribute("dataArray", dataArray);
		model.addAttribute("user", user);
		model.addAttribute("page", page);
		return "modules/sys/officeChargePersonList";
	}

	@ResponseBody
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = { "listData" })
	public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		return page;
	}

	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany() == null || user.getCompany().getId() == null) {
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice() == null || user.getOffice().getId() == null) {
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("user", user);
		// 返回可用的角色列表
		List<Role> findAllRole = systemService.findAllRole();
		List<Role> collect = findAllRole.stream().filter(role -> "1".equals(role.getUseable()))
				.collect(Collectors.toList());
		model.addAttribute("allRoles", collect);
		return "modules/sys/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		// 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		if (!beanValidator(model, user)) {
			return form(user, model);
		}
		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))) {
			String i18nTip = I18n.i18nMessage("user.saveUserFailure");
			Object[] params = { user.getLoginName() };
			String content = MessageFormat.format(i18nTip, params);
			addMessage(model, content);
			return form(user, model);
		}
		// 角色数据有效性验证，过滤不在授权内的角色
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()) {
			if (roleIdList.contains(r.getId())) {
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		List<String> oldRole = null;
		if (UserUtils.getUser().getId().equals(user.getId())) {
			// 修改后的角色
			oldRole = systemService.findtUserRole(user.getId());
		}
		// 保存用户信息
		systemService.saveUser(user);
		// 清除当前用户缓存
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
			UserUtils.clearCache();
			// UserUtils.getCacheMap().clear();
		}

		String i18nTip = I18n.i18nMessage("user.saveUserTip");
		Object[] params = { user.getLoginName() };
		String content = MessageFormat.format(i18nTip, params);
		addMessage(redirectAttributes, content);
		// 如果用户修改自己的登录名和角色时，则跳转到登录页面重新登录
		if (UserUtils.getUser().getId().equals(user.getId())) {
			// 用户之前的角色
			List<String> newRole = user.getRoleIdList();
			if (UserUtils.getUser().getId().equals(user.getId())) {
				if (!(newRole.containsAll(oldRole) && oldRole.containsAll(newRole))
						|| !(user.getLoginName().equals(user.getOldLoginName()))) {
					UserUtils.getSubject().logout();
				}
			}
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "saves")
	public String saves(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		user.setCompany(null);
		user.setOffice(null);
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		String i18nTip = "";
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		if (UserUtils.getUser().getId().equals(user.getId())) {
			i18nTip = I18n.i18nMessage("user.deleteUserFailerTip");
			addMessage(redirectAttributes, i18nTip);
		} else if (User.isAdmin(user.getId())) {
			i18nTip = I18n.i18nMessage("user.deleteAdminFailerTip");
			addMessage(redirectAttributes, i18nTip);
		} else {
			systemService.deleteUser(user);
			i18nTip = I18n.i18nMessage("user.deleteUserSuccessful");
			addMessage(redirectAttributes, i18nTip);
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 导出用户数据
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(User user, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
			List<User> list = page.getList();
			for (User userRole : list) {
				List<Role> roleList = systemService.findRoleName(userRole);
				userRole.setRoleList(roleList);
			}
			new ExportExcel("用户数据", User.class, 2).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			String i18nTip = I18n.i18nMessage("user.exportUserFailer");
			Object[] params = { e.getMessage() };
			String content = MessageFormat.format(i18nTip, params);
			addMessage(redirectAttributes, content);
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<User> list = ei.getDataList(User.class);
			for (User user : list) {
				try {
					if ("true".equals(checkLoginName("", user.getLoginName()))) {
						Office company = user.getCompany();
						Office office = user.getOffice();
						if (company == null) {
							Office companyNull = new Office();
							companyNull.setId("");
							user.setCompany(companyNull);
						}
						if (office == null) {
							Office officeNull = new Office();
							officeNull.setId("");
							user.setOffice(officeNull);
							String entryptPassword = SystemService.entryptPassword(user.getPassword());
							user.setPassword(entryptPassword);
							BeanValidators.validateWithException(validator, user);
							systemService.saveUser(user);
							successNum++;
						} else if (!("1").equals(office.getGrade())) {
							failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：只允许导入一级部门");
							failureNum++;
						} else {
							String entryptPassword = SystemService.entryptPassword(user.getPassword());
							user.setPassword(entryptPassword);
							BeanValidators.validateWithException(validator, user);
							systemService.saveUser(user);
							successNum++;
						}
					} else {
						failureMsg.append("<br/>登录名 " + user.getLoginName() + " 已存在; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条信息，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条用户" + failureMsg);
		} catch (Exception e) {
			String i18nTip = I18n.i18nMessage("user.importUserFailer");
			Object[] params = { e.getMessage() };
			String content = MessageFormat.format(i18nTip, params);
			addMessage(redirectAttributes, content);
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 下载导入用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "用户数据导入模板.xlsx";
			List<User> list = Lists.newArrayList();
			User user = UserUtils.getUser();
			User newUser = new User();
			newUser.setLoginName(user.getLoginName());
			newUser.setId(user.getId());
			newUser.setName("Tom");
			newUser.setMobile("13169921844");
			newUser.setCompany(user.getCompany());
			newUser.setOffice(user.getOffice());
			newUser.setPassword("123456");
			newUser.setNo(user.getNo());
			newUser.setEmail("123@163.com");
			newUser.setPhone("123412341234");
			List<Role> roleList = user.getRoleList();
			if (!roleList.isEmpty()) {
				Role role = roleList.get(0);
				role.setName("app管理员（三洋）,ERP管理员");
				newUser.setRoleList(roleList);
			}
			list.add(newUser);
			new ExportExcel("用户数据", User.class, 2).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			String i18nTip = I18n.i18nMessage("user.importFailer");
			Object[] params = { e.getMessage() };
			String content = MessageFormat.format(i18nTip, params);
			addMessage(redirectAttributes, content);
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}

	/**
	 * 验证登录名是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName != null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName != null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * 用户信息显示及保存
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "info")
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userInfo";
			}
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			String i18nTip = I18n.i18nMessage("user.saveUserInfoSuccessful");
			model.addAttribute("message", i18nTip);
		}
		model.addAttribute("user", currentUser);
		model.addAttribute("Global", new Global());
		return "modules/sys/userInfo";
	}

	/**
	 * 返回用户信息
	 * 
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "infoData")
	public User infoData() {
		return UserUtils.getUser();
	}

	/**
	 * 修改个人用户密码
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
			if (Global.isDemoMode()) {
				model.addAttribute("message", "演示模式，不允许操作！");
				return "modules/sys/userModifyPwd";
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())) {
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				String i18nTip = I18n.i18nMessage("user.modifyPassWordSuccessful");
				model.addAttribute("message", i18nTip);
			} else {
				String i18nTip = I18n.i18nMessage("user.modifyPassWordFailure");
				model.addAttribute("message", i18nTip);
			}
		}
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}

	/*
	 * @RequiresPermissions("user")
	 * 
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "treeData") public List<Map<String, Object>>
	 * treeData(@RequestParam(required = false) String officeId, HttpServletResponse
	 * response) { List<Map<String, Object>> mapList = Lists.newArrayList();
	 * List<User> list = systemService.findUserByOfficeId(officeId); for (int i = 0;
	 * i < list.size(); i++) { User e = list.get(i); Map<String, Object> map =
	 * Maps.newHashMap(); map.put("id", "u_" + e.getId()); map.put("pId", officeId);
	 * map.put("name", StringUtils.replace(e.getName(), " ", "")); mapList.add(map);
	 * } return mapList; }
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUser(new User());
		for (int i = 0; i < list.size(); i++) {
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", e);
			map.put("pIds", 0);
			map.put("text", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}

	@ResponseBody
	@RequestMapping(value = "test")
	public void test(HttpServletResponse response) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int k = 0; k <= 10000; k++) {

					Long incr = EhCacheUtils.incr("ddd");
					System.out.println(incr);
				}

			}
		}).start();

	}

	// @InitBinder
	// public void initBinder(WebDataBinder b) {
	// b.registerCustomEditor(List.class, "roleList", new PropertyEditorSupport(){
	// @Autowired
	// private SystemService systemService;
	//
	// public void setAsText(String text) throws IllegalArgumentException {
	// String[] ids = StringUtils.split(text, ",");
	// List<Role> roles = new ArrayList<Role>();
	// for (String id : ids) {
	// Role role = systemService.getRole(Long.valueOf(id));
	// roles.add(role);
	// }
	// setValue(roles);
	// }
	//
	// public String getAsText() {
	// return Collections3.extractToString((List) getValue(), "id", ",");
	// }
	// });
	// }
}
