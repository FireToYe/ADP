package com.zhilink.srm.manager.modules.sys.web.api;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhilink.manager.common.config.PropertiesHolder;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.basemodel.PageApiResult;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.exception.CommonException;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.entity.Office;
import com.zhilink.srm.manager.modules.sys.entity.Role;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.enums.DataScopeEnum;
import com.zhilink.srm.manager.modules.sys.enums.RoleTypeEnum;
import com.zhilink.srm.manager.modules.sys.enums.UserTypeEnum;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.FileUtils;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

import eu.bitwalker.useragentutils.Application;

/***
 * @ClassName: AuthorityController
 * @Description: 供应商权限管理
 * @author an48huf
 * @date 2017年11月17日
 *
 */
@Controller
@RequestMapping(value = "${apiPath}/authority")
public class AuthorityController extends BaseController {

	private final static String ERRORCODE = "-1";

	private final static String SUPPLIERMENUIDS = "0,1,10005,";// 供应商模块的权限

	private final static String SUPPLIEAUTHORITYRMENUS = "100050018";
	@Autowired
	private SystemService systemService;

	/**
	 * 修改用户部分更新
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@ModelAttribute("User")
	public User initUser(@RequestParam(required = false) String id, HttpServletRequest request) {
		if (request.getRequestURI().indexOf("modifyUser") > -1 || request.getRequestURI().indexOf("updateUser") > -1) {
			if (StringUtils.isNotBlank(id)) {
				return systemService.getUser(id);
			} else {
				return new User();
			}
		}
		return null;
	}

	@RequestMapping(value = "/getRoleList")
	public @ResponseBody ResultModel getRoleList(Role role, Model model) {

		ResultModel result = new ResultModel();
		// 获取用户的一级组织
		String orgIdLev1 = getLev1OrgId();
		if (orgIdLev1.length() == 0) {
			result.setErrorCode("-1");
			result.setErrorMsg(I18n.i18nMessage("api.userUnmainteinOrgalization"));
			return result;
		}

		// 获取公共角色和当前一级组织架构下的所有角色
		List<Role> roleList = systemService.getRoleListForOrg(orgIdLev1);
		List<JSONObject> listRole = new ArrayList<JSONObject>();
		if (roleList != null && !roleList.isEmpty()) {
			for (Role r : roleList) {
				if (StringUtils.isNotBlank(r.getUseable()) && !"1".equals(r.getUseable())) {
					continue;

				}
				// 过滤掉任务分配和管理角色，不显示在SRM
				if (RoleTypeEnum.ASSIGNMENT.getValue().equals(r.getRoleType())
						|| RoleTypeEnum.SECURITYROLE.getValue().equals(r.getRoleType())) {
					continue;
				}

				JSONObject json = new JSONObject();
				json.put("id", r.getId());
				json.put("roleType", r.getRoleType());
				json.put("roleTypeName", RoleTypeEnum.getRoleTypeName(r.getRoleType()));
				json.put("name", r.getName());
				json.put("enname", r.getEnname());
				json.put("officeId", r.getOffice().getId());
				json.put("officeName", r.getOffice().getName());
				listRole.add(json);
			}
		}
		result.setBody(listRole);
		return result;
	}

	/**
	 * 删除角色,检查是否被引用
	 * 
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/deleteRole")
	public @ResponseBody ResultModel deleteRole(Role role, Model model) {
		ResultModel result = new ResultModel();

		if (role == null || StringUtils.isBlank(role.getId())) {
			result.setErrorCode("901");
			result.setErrorMsg(I18n.convert(901));
			return result;
		}
		// 验证角色是否被引用
		Role role2 = systemService.getRole(role.getId());
		if (RoleTypeEnum.PUBLICROLE.getValue().equals(role2.getRoleType())) {
			result.setErrorCode("-1");
			result.setErrorMsg(I18n.i18nMessage("api.publicRoleUnpermitDelete"));
			return result;
		}
		// 删除角色
		systemService.deleteRole(role);
		return result;
	}

	/**
	 * 新增供应商角色,是否系统数据后台赋值
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/addRole", method = { RequestMethod.POST })
	public @ResponseBody ResultModel addRole(Role role, HttpServletRequest request, HttpServletResponse response) {

		ResultModel result = validateRole(role);
		if (ERRORCODE.equals(result.getHead().getErrorCode())) {
			return result;
		}
		String parameter = request.getParameter("menuIdsList");
		if (StringUtils.isNotBlank(parameter)) {
			role.setMenuList(setMenuIdsList(parameter));
		}

		// 角色的菜单验证是否超出权限
		if (role.getMenuList() != null && role.getMenuList().size() > 0) {
			if (!UserUtils.getUser().isAdmin()) {
				valiExceededPermiss(role.getMenuList(), result);
				if (ERRORCODE.equals(result.getHead().getErrorCode())) {
					return result;
				}

			}
			// 添加供应商模块的父级ID
			String[] splitMenus = SUPPLIERMENUIDS.split(",");
			for (String strMenu : splitMenus) {
				if (StringUtils.isNotBlank(strMenu)) {
					Menu m = new Menu(strMenu);
					role.getMenuList().add(m);
				}
			}
		}

		role.setSysData("0");// 表示拥有角色修改人员的权限都能进行修改
		Office office = new Office();
		office.setId(getLev1OrgId());
		role.setOffice(office);
		role.setRoleType("user");// 普通角色
		role.setUseable("1");// 可用
		role.setDataScope("1");// 所有数据

		systemService.saveRole(role);
		return result;
	}

	@RequestMapping(value = "/getRole")
	public @ResponseBody ResultModel getRole(String id) {

		ResultModel result = new ResultModel();
		if (StringUtils.isBlank(id)) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.roleIdUnpermitNull"));
			return result;
		}
		Role role = systemService.getRole(id);
		result.setBody(role);
		return result;
	}

	/**
	 * 修改供应商角色
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/modifyRole", method = { RequestMethod.POST })
	public @ResponseBody ResultModel modifyRole(@ModelAttribute("Role") Role role, HttpServletRequest request) {

		ResultModel result = validateRole(role);
		if (ERRORCODE.equals(result.getHead().getErrorCode())) {
			return result;
		}
		if (StringUtils.isBlank(role.getId())) {
			result.setErrorCode("-1");
			result.setErrorMsg(I18n.i18nMessage("api.roleIdUnpermitNull"));
			return result;
		}
		String parameter = request.getParameter("menuIdsList");
		if (StringUtils.isNotBlank(parameter)) {
			role.setMenuList(setMenuIdsList(parameter));
		}
		// 角色的菜单验证是否超出权限
		if (role.getMenuList() != null && role.getMenuList().size() > 0) {
			if (!UserUtils.getUser().isAdmin()) {
				valiExceededPermiss(role.getMenuList(), result);
			}
		}
		// 添加供应商模块的父级ID
		String[] splitMenus = SUPPLIERMENUIDS.split(",");
		for (String strMenu : splitMenus) {
			if (StringUtils.isNotBlank(strMenu)) {
				Menu m = new Menu(strMenu);
				role.getMenuList().add(m);
			}
		}
		if (ERRORCODE.equals(result.getHead().getErrorCode())) {
			return result;
		}
		Role oldRole = systemService.getRole(role.getId());
		role.setOffice(oldRole.getOffice());
		role.setRoleType(oldRole.getRoleType());

		// 如果角色本身有SRM权限管理页面，则加上，权限管理页面只能后台赋值，SRM过滤掉
		List<Menu> collect = oldRole.getMenuList().stream()
				.filter(menu -> menu.getId().indexOf(SUPPLIEAUTHORITYRMENUS) >= 0).collect(Collectors.toList());
		if (collect != null && !collect.isEmpty()) {
			role.getMenuList().addAll(collect);
		}
		if (!RoleTypeEnum.USER.getValue().equals(role.getRoleType())) {
			result.setErrorCode("-1");
			result.setErrorMsg(I18n.i18nMessage("api.SrmUnpermitEditUncommonRole"));
			return result;
		}

		role.setUseable(oldRole.getUseable());// 可用
		role.setDataScope(oldRole.getDataScope());// 所有数据
		systemService.saveRole(role);
		return result;
	}

	/**
	 * 获取角色类型列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getRoleTypeList")
	public @ResponseBody ResultModel getRoleTypeList() {
		ResultModel result = new ResultModel();
		result.setBody(RoleTypeEnum.roleTypeToList());
		return result;
	}

	/**
	 * 获取数据权限列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getDataScopeList")
	public @ResponseBody ResultModel getDataScopeList() {

		ResultModel result = new ResultModel();
		result.setBody(DataScopeEnum.dataScopeToList());
		return result;
	}

	/**
	 * 获取当前组织和当前组织所有子组织的用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getOrgUserList")
	public @ResponseBody ResultModel getOrgUserList(int pageSize, int pageNo) {

		String officeId = getLev1OrgId();
		ResultModel result = new ResultModel();
		String orgIdLev1 = officeId;
		if (orgIdLev1.length() == 0) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.userUnmainteinOrgalization"));
			return result;
		}
		Page<User> page = new Page<User>();

		page.setPageSize(pageSize);
		page.setPageNo(pageNo);
		List<String> includes = Lists.newArrayList();
		includes.add(UserTypeEnum.GENERALUSER.getValue());
		page = systemService.findPageUserByParentOfficeId(page, officeId, includes);
		PageApiResult<JSONObject> pageDate = new PageApiResult<JSONObject>();
		if (page.getList() != null) {
			List<User> findList = page.getList();
			pageDate.setTotolCount((int) page.getCount());
			List<JSONObject> userArray = new ArrayList<JSONObject>();
			for (User user : findList) {
				// 过滤掉系统用户，部门经理、一级组织架构管理员
				if (!(StringUtils.isBlank(user.getUserType())
						|| UserTypeEnum.GENERALUSER.getValue().equals(user.getUserType()))) {
					continue;
				}
				JSONObject jsonOb = new JSONObject();
				jsonOb.put("companyId", user.getCompany() != null ? user.getCompany().getId() : "");
				jsonOb.put("companyName", user.getCompany() != null ? user.getCompany().getName() : "");
				jsonOb.put("officeId", user.getOffice() != null ? user.getOffice().getId() : "");
				jsonOb.put("officeName", user.getOffice() != null ? user.getOffice().getName() : "");
				jsonOb.put("loginName", user.getLoginName());
				jsonOb.put("name", user.getName());
				jsonOb.put("id", user.getId());
				jsonOb.put("photo", user.getPhoto());
				jsonOb.put("mobile", user.getMobile());
				userArray.add(jsonOb);
			}
			pageDate.setListdata(userArray);
		}
		result.setBody(pageDate);
		return result;
	}

	@RequestMapping(value = "/addUser", method = { RequestMethod.POST })
	public @ResponseBody ResultModel addUser(User user, HttpServletRequest request) {

		ResultModel result = validateRole(user);
		if (ERRORCODE.equals(result.getHead().getErrorCode())) {
			return result;
		}
		// 登录名是否重复
		result = checkLoginName(user.getLoginName(), request);
		if (ERRORCODE.equals(result.getHead().getErrorCode())) {
			return result;
		}
		String confirmNewPassword = request.getParameter("confirmNewPassword");
		String newPassword = request.getParameter("newPassword");
		if (StringUtils.isBlank(confirmNewPassword) || StringUtils.isBlank(newPassword)) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.passwordNotNull"));
			return result;
		}
		if (!confirmNewPassword.equals(newPassword)) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.passwordUncoinside"));
			return result;
		}
		// SRM前端角色使用了字符串
		String roleIds = request.getParameter("roleIds");
		if (StringUtils.isNotBlank(roleIds)) {
			user.setRoleList(setRoleIds(roleIds));
		}
		if (StringUtils.isNotBlank(newPassword)) {
			user.setPassword(SystemService.entryptPassword(newPassword));
		}

		Office company = new Office();
		company.setId(getLev1OrgId());
		user.setCompany(company);
		user.setOffice(company);
		user.setUserType("3");// 普通用户

		systemService.saveUserNoRequireRole(user);

		return result;
	}

	@RequestMapping(value = "/modifyUser", method = { RequestMethod.POST })
	public @ResponseBody ResultModel modifyUser(@ModelAttribute("User") User user, HttpServletRequest request) {

		ResultModel result = validateRole(user);
		if (ERRORCODE.equals(result.getHead().getErrorCode())) {
			return result;
		}

		if (StringUtils.isBlank(user.getId())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.roleIdUnpermitNull"));
		}
		String confirmNewPassword = request.getParameter("confirmNewPassword");
		String newPassword = request.getParameter("newPassword");
		if (StringUtils.isNotBlank(confirmNewPassword) && StringUtils.isNotBlank(newPassword)) {
			if (!confirmNewPassword.equals(newPassword)) {
				result.setErrorCode(ERRORCODE);
				result.setErrorMsg(I18n.i18nMessage("api.passwordUncoinside"));
				return result;
			}
			user.setPassword(SystemService.entryptPassword(newPassword));
		}

		// SRM前端角色使用了字符串, "roleIds":
		// "[\"b70e049ed8fd41ad971fcb2dfb2a4020\",\"8706a6a8b4f14982bfe50e2f3ec9ba7a\"]"
		String roleIds = request.getParameter("roleIds");
		if (StringUtils.isNotBlank(roleIds)) {
			user.setRoleList(setRoleIds(roleIds));
		}
		user.setUserType("3");// 普通用户
		systemService.saveUserNoRequireRole(user);

		return result;
	}

	@RequestMapping(value = "/deleteUser")
	public @ResponseBody ResultModel deleteUser(String id) {

		ResultModel result = new ResultModel();
		User curuser = UserUtils.getUser();
		if (curuser.getId().equals(id)) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.deleteUserFail"));
			return result;
		} else if (User.isAdmin(id)) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.deleteAdminFail"));
			return result;
		}

		// 非本组织用户无权删除
		if (!curuser.isAdmin()) {
			boolean allowable = false;
			List<User> userByParentOfficeId = systemService.findUserByParentOfficeId(getLev1OrgId());
			if (userByParentOfficeId != null && !userByParentOfficeId.isEmpty()) {
				for (User u : userByParentOfficeId) {
					if (u.getId().equals(id)) {
						allowable = true;
					}
				}
			}
			if (!allowable) {
				result.setErrorCode(ERRORCODE);
				result.setErrorMsg(I18n.i18nMessage("api.deleteOrganizationUserFail"));
				return result;
			}
		}
		User user = new User();
		user.setId(id);
		systemService.deleteUser(user);

		return result;
	}

	@RequestMapping(value = "/getUser")
	public @ResponseBody ResultModel getUser(String id) {

		ResultModel result = new ResultModel();
		if (StringUtils.isBlank(id)) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.roleIdUnpermitNull"));
			return result;
		}

		User user = systemService.getUser(id);
		result.setBody(user);
		return result;
	}

	/**
	 * api获取用户菜单权限
	 */
	@RequestMapping(value = "/getMenuList")
	@ResponseBody
	public ResultModel getMenuList() {
		try {
			ResultModel result = new ResultModel();

			List<Menu> allMenus = UserUtils.getMenuList();
			List<Menu> menuList = Lists.newArrayList();
			if (allMenus != null && allMenus.size() > 0) {
				for (Menu menu : allMenus) {
					// srm供应商菜单，其他的不返回
					if (menu.getMenuType() == 1 && menu.getParentIds() != null
							&& menu.getParentIds().indexOf(SUPPLIERMENUIDS) >= 0) {

						// 过滤掉供应商权限管理菜单，只能在后台设置
						if (menu.getId().indexOf(SUPPLIEAUTHORITYRMENUS) >= 0) {
							continue;
						}

						menuList.add(menu);
					}
				}
			}

			result.setBody(menuList);
			return result;
		} catch (Exception ex) {//
			ex.printStackTrace();
			throw new CommonException();
		}
	}

	/**
	 * 验证登录名是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkLoginName", method = { RequestMethod.POST })
	public ResultModel checkLoginName(String loginName, HttpServletRequest request) {

		ResultModel resultModel = new ResultModel();
		if (StringUtils.isBlank(loginName)) {
			resultModel.setErrorCode(ERRORCODE);
			resultModel.setErrorMsg(I18n.i18nMessage("api.appAuditParamMiss"));
		}

		User u = systemService.getUserByLoginName(loginName);
		if (u != null) {
			resultModel.setErrorCode(ERRORCODE);
			resultModel.setErrorMsg(I18n.i18nMessage("api.loginName") + loginName + I18n.i18nMessage("api.exsit"));
		}
		return resultModel;

	}

	@ResponseBody
	@RequestMapping(value = "/checkRoleIfUsed")
	public ResultModel checkRoleIfUsed(String id, HttpServletRequest request) {

		ResultModel resultModel = new ResultModel();
		if (StringUtils.isBlank(id)) {
			resultModel.setErrorCode(ERRORCODE);
			resultModel.setErrorMsg(I18n.i18nMessage("api.appAuditParamMiss"));
			return resultModel;
		}
		boolean ifUsed = systemService.checkRoleIfUsed(id);
		Map map = new HashMap<String, String>();
		if (ifUsed) {
			map.put("ifUsed", true);
		} else {
			map.put("ifUsed", false);
		}

		resultModel.setBody(map);
		return resultModel;
	}

	private ResultModel validateRole(User user) {

		ResultModel result = new ResultModel();
		if (user == null) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.appAuditParamMiss"));
		}
		if (StringUtils.isBlank(user.getName())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.userNameFull"));
		} else if (StringUtils.isBlank(user.getNo())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.userNoFull"));
		} else if (StringUtils.isBlank(user.getLoginName())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.userLoginNameFull"));
		} else if (StringUtils.isBlank(user.getLoginFlag())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.userLoginFlagFull"));
		} else if (StringUtils.isBlank(user.getEnt())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.defaultEntertipeNoFull"));
		}

		return result;
	}

	/**
	 * 验证角色参数是否为空
	 * 
	 * @param role
	 * @return
	 */
	private ResultModel validateRole(Role role) {

		ResultModel result = new ResultModel();
		if (role == null) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.appAuditParamMiss"));
		}
		if (StringUtils.isBlank(role.getName())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.roleNameIsFull"));
		} else if (StringUtils.isBlank(role.getDataScope())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.roleDataRealmFull"));
		} else if (StringUtils.isBlank(role.getUseable())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.roleUserOrNot"));
		}

		return result;
	}

	/**
	 * 验证添加角色赋值菜单是否越权
	 * 
	 * @param currMenus
	 * @param resultModel
	 */
	private void valiExceededPermiss(List<Menu> currMenus, ResultModel resultModel) {

		List<Menu> allMenus = UserUtils.getMenuList();
		HashSet<String> menuIds = new HashSet<String>();
		if (allMenus != null && allMenus.size() > 0) {
			for (Menu menu : allMenus) {
				if (menu.getMenuType() == 1) {
					menuIds.add(menu.getId());
				}
			}
		}

		for (Menu cur : currMenus) {
			if (!StringUtils.isBlank(cur.getId())) {
				String[] split = SUPPLIERMENUIDS.split(",");
				for (String base : split) {
					if (base.length() == 0 || base.equals(cur.getId())) {
						continue;
					}
				}

				if (!menuIds.contains(cur.getId().trim())) {
					resultModel.setErrorCode(ERRORCODE);
					resultModel.setErrorMsg(I18n.i18nMessage("api.noAthorization") + cur.getId());
				}
			}
		}
	}

	/**
	 * 适配SRM前端传过来的数组字符串
	 * 
	 * @param menuIdsList
	 */
	public List<Menu> setMenuIdsList(String menuIdsList) {

		menuIdsList = StringEscapeUtils.unescapeHtml4(menuIdsList);
		JSONArray parseArray = JSON.parseArray(menuIdsList);
		List<Menu> menuList = new ArrayList<Menu>();
		for (Object menuId : parseArray) {
			Menu menu = new Menu();
			menu.setId(menuId.toString());
			menuList.add(menu);
		}
		return menuList;
	}

	/**
	 * 适配SRM前端角色ids数组字符串
	 * 
	 * @param roleIds
	 */
	public List<Role> setRoleIds(String roleIds) {
		roleIds = StringEscapeUtils.unescapeHtml4(roleIds);
		JSONArray parseArray = JSON.parseArray(roleIds);
		List<Role> roleList = new ArrayList<Role>();
		for (Object roleId : parseArray) {
			Role role = new Role();
			role.setId(roleId.toString());
			roleList.add(role);
		}
		return roleList;
	}

	/**
	 * 获取当前用户的一级组织架构
	 * 
	 * @return
	 */
	private String getLev1OrgId() {
		// 当天用户
		User user = UserUtils.getUser();
		// 获取用户的一级组织
		String orgIdLev1 = "";
		if (user.getOffice() != null && StringUtils.isNotBlank(user.getOffice().getId())) {
			String parentIds = user.getOffice().getParentIds();
			// 0,1,
			if (!StringUtils.isBlank(parentIds)) {
				int start = parentIds.indexOf(",");
				int lastIndex = parentIds.lastIndexOf(",");
				if (start > 0 && start != lastIndex) {
					orgIdLev1 = parentIds.substring(start + 1, parentIds.indexOf(",", start + 1));
				} else {
					orgIdLev1 = user.getOffice().getId();
				}
			} else {
				orgIdLev1 = user.getOffice().getId();
			}
		} else {
			String parentIds = user.getCompany().getParentIds();
			// 0,1,
			if (!StringUtils.isBlank(parentIds)) {
				int start = parentIds.indexOf(",");
				int lastIndex = parentIds.lastIndexOf(",");
				if (start > 0 && start != lastIndex) {
					orgIdLev1 = parentIds.substring(start + 1, parentIds.indexOf(",", start + 1));
				} else {
					orgIdLev1 = user.getCompany().getId();
				}
			} else {
				orgIdLev1 = user.getCompany().getId();
			}
		}

		return orgIdLev1;

	}
	@RequestMapping(value = "/updateUser", method = { RequestMethod.POST })
	public @ResponseBody ResultModel updateUser(@ModelAttribute("User") User user, HttpServletRequest request,@RequestParam(value = "photos") MultipartFile[] files,HttpServletResponse response) {
		ResultModel result = validateRole(user);
		List<String> pathList = new ArrayList<String>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (files.length == 0) {
			return result;
		}
		String filePath = null;
		String RespfilePath = null;
		try {
			String userfilesBaseDir = Global.getUserfilesBaseDir();
			if (StringUtils.isBlank(userfilesBaseDir)) {
				userfilesBaseDir = PropertiesHolder.getADPHome();
			}
			String path = user.getId()+"/images/photo/";
			String originalFilename =null;
			//遍历输入流
			for (int i = 0; i < files.length; i++) {
				
				String random = this.randomWords(); //生成5个随机字母以内的随机数
				//用于替换原文件名
				String dateTimestamp = new SimpleDateFormat("yyyyMMddHHsss").format(new Date()) + random;
				InputStream stream = files[i].getInputStream();
				originalFilename =  files[i].getOriginalFilename();			
				String filePrefix = originalFilename.substring(originalFilename.indexOf("."), originalFilename.length());			
				originalFilename =dateTimestamp;
				//防止重复的文件基本名
				String dateFormatPath = new SimpleDateFormat("yyyy").format(new Date()) + "/"
						+ new SimpleDateFormat("MM").format(new Date()) + "/" + originalFilename+filePrefix;
				String userfilesBaseDirFormat = userfilesBaseDir.substring(0, userfilesBaseDir.length());
				//写进服务器的路径(包括文件名)
				filePath = userfilesBaseDirFormat + Global.USERFILES_BASE_URL + path + dateFormatPath;
				//返回的访问路径名
				RespfilePath = request.getContextPath()+ Global.USERFILES_BASE_URL + path + dateFormatPath;
				// 保存图片到某个目录中
				FileUtils.saveFile(stream, filePath);
				if (null != RespfilePath) {
					
					pathList.add(RespfilePath);
				}				
			}
		} catch (Exception e) {
			e.getMessage();
			result.setErrorCode("30");
			result.setErrorMsg("上传图片失败");
		}
		if (ERRORCODE.equals(result.getHead().getErrorCode())) {
			return result;
		}

		if (StringUtils.isBlank(user.getId())) {
			result.setErrorCode(ERRORCODE);
			result.setErrorMsg(I18n.i18nMessage("api.roleIdUnpermitNull"));
		}
		String confirmNewPassword = request.getParameter("confirmNewPassword");
		String newPassword = request.getParameter("newPassword");
		if (StringUtils.isNotBlank(confirmNewPassword) && StringUtils.isNotBlank(newPassword)) {
			if (!confirmNewPassword.equals(newPassword)) {
				result.setErrorCode(ERRORCODE);
				result.setErrorMsg(I18n.i18nMessage("api.passwordUncoinside"));
				return result;
			}
			user.setPassword(SystemService.entryptPassword(newPassword));
		}

		// SRM前端角色使用了字符串, "roleIds":
		// "[\"b70e049ed8fd41ad971fcb2dfb2a4020\",\"8706a6a8b4f14982bfe50e2f3ec9ba7a\"]"
		String roleIds = request.getParameter("roleIds");
		if (StringUtils.isNotBlank(roleIds)) {
			user.setRoleList(setRoleIds(roleIds));
		}
		user.setUserType("1");// 系统管理员
		if(pathList.size()== 0){
			user.setPhoto(null);
		}else{
			user.setPhoto(pathList.get(0));
			resultMap.put("url", pathList.get(0));
			result.setBody(resultMap);
		}
		
		systemService.saveUserNoRequireRole(user);
		return result;
	}
	public String randomWords(){		
		List<Object> list = new ArrayList<Object>();
        for (char c = 'a'; c <= 'z'; c++) {
            list.add(c);
        }
        String str = "";
        for (int i = 0; i < 5; i++) {
            int num = (int) (Math.random() * 26);
            str = str + list.get(num);
        }
        return str;
	}
}
