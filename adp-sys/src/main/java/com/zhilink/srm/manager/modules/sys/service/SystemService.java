/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.zhilink.manager.framework.common.security.Digests;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.Encodes;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.Servlets;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.security.shiro.session.SessionDAO;
import com.zhilink.srm.common.service.BaseService;
import com.zhilink.srm.common.service.ServiceException;
import com.zhilink.srm.manager.modules.sys.dao.MenuDao;
import com.zhilink.srm.manager.modules.sys.dao.RoleDao;
import com.zhilink.srm.manager.modules.sys.dao.UserDao;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.entity.Office;
import com.zhilink.srm.manager.modules.sys.entity.Role;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.enums.UserTypeEnum;
import com.zhilink.srm.manager.modules.sys.security.SystemAuthorizingRealm;
import com.zhilink.srm.manager.modules.sys.utils.LogUtils;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author jaray
 * 
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	private static final String initialValue = "0001";

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	@Autowired
	private SystemAuthorizingRealm systemRealm;

	// @Autowired
	// private JdbcTemplate jdbcTemplate;

	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	// -- User Service --//

	/**
	 * 获取用户
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}

	/**
	 * 根据角色id是否被用户引用
	 * 
	 * @param id
	 * @return
	 */
	public boolean checkRoleIfUsed(String id) {

		int countRoleIfUsed = roleDao.countRoleIfUsed(id);
		if (countRoleIfUsed > 0) {
			return true;
		}
		return false;
	}

	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.findList(user));
		return page;
	}

	public List<Role> findRoleName(User user) {
		List<Role> list = roleDao.findRoleName(user);
		return list;
	}

	/**
	 * 无分页查询人员列表
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}

	/***
	 * 无分页查询人员列表，无数据范围过滤
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUserNoDataScope(User user) {
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>) CacheUtils.get(UserUtils.USER_CACHE,
				UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null) {
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			userDao.insert(user);
		} else {
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
				CacheUtils.remove(UserUtils.USER_CACHE,
						UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				userDao.insertUserRole(user);
			} else {
				throw new ServiceException(user.getLoginName() + "没有设置角色或角色输入错误！");
			}
			// 将当前用户同步到Activiti
			// saveActivitiUser(user);
			// 清除用户缓存
			UserUtils.clearCache(user);
			// // 清除权限缓存
			// systemRealm.clearAllCachedAuthorizationInfo();
		}
	}

	/***
	 * 保存用户不强制要求设置角色
	 * 
	 * @param user
	 */
	@Transactional(readOnly = false)
	public void saveUserNoRequireRole(User user) {
		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			userDao.insert(user);
		} else {
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
				CacheUtils.remove(UserUtils.USER_CACHE,
						UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				userDao.insertUserRole(user);
			}
			// 清除用户缓存
			UserUtils.clearCache(user);
		}
	}

	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		// 同步到Activiti
		// deleteActivitiUser(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(entryptPassword(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(StringUtils.getRemoteAddr(Servlets.getRequest()));
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	/**
	 * 获得活动会话
	 * 
	 * @return
	 */
	public Collection<Session> getActiveSessions() {
		return sessionDao.getActiveSessions(false);
	}

	// -- Role Service --//

	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}

	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}

	public List<Role> findRole(Role role) {
		return roleDao.findList(role);
	}

	public List<Role> findAllRole() {
		return UserUtils.getRoleList();
	}

	public List<String> findtUserRole(String id) {
		return userDao.findtUserRole(id);
	}

	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		if (StringUtils.isBlank(role.getId())) {
			role.preInsert();
			roleDao.insert(role);
			// 同步到Activiti
			// saveActivitiGroup(role);
		} else {
			role.preUpdate();
			roleDao.update(role);
		}
		// 更新角色与菜单关联
		roleDao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0) {
			roleDao.insertRoleMenu(role);
		}
		// 更新角色与部门关联
		roleDao.deleteRoleOffice(role);
		if (role.getOfficeList().size() > 0) {
			roleDao.insertRoleOffice(role);
		}
		// 同步到Activiti
		// saveActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 同步到Activiti
		// deleteActivitiGroup(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	/**
	 * 删除角色，并且所有关联信息
	 * 
	 * @param role
	 */
	@Transactional(readOnly = false)
	public void deleteRoleAndRelationt(Role role) {

		/*
		 * roleDao.deleteUserRole(role); roleDao.deleteRoleMenu(role);
		 * roleDao.deleteRoleOffice(role);
		 */
		// 软删除
		roleDao.delete(role);

		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
	}

	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles) {
			if (e.getId().equals(role.getId())) {
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null) {
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	// -- Menu Service --//

	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu() {
		return UserUtils.getMenuList();
	}

	// 菜单ID生成器
	public void createNewId(Menu menu) {

		String leftPartId = menu.getParentId();
		System.out.println(menu.getParentId());
		String newId = menuDao.findMaxId(leftPartId, menu.getDbName());
		if (StringUtils.isEmpty(newId)) {
			newId = leftPartId + initialValue;
		} else {
			try {
				long tmpId = Long.parseLong(newId);
				tmpId++;
				newId = "" + tmpId;
			} catch (NumberFormatException e) {
				logger.warn("菜单最大ID非整数,id:" + newId);
			}

		}
		menu.setId(newId);

	}

	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {

		// 如果為空則一定是新增菜單
		if (StringUtils.isBlank(menu.getId())) {
			// 获取父节点实体
			menu.setParent(this.getMenu(menu.getParent().getId()));
			// 获取修改前的parentIds，用于更新子节点的parentIds
			// String oldParentIds = menu.getParentIds();
			// 设置新的父节点串
			menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

			menu.preInsert();
			createNewId(menu);
			// 设置新的父节点串
			menuDao.insert(menu);
		}

		// 如果傳過來父id和依照子id查詢到的父id不一致,那也視為新增
		else if (!menu.getParent().getId().equals(this.getMenu(menu.getId()).getParentId())) {
			// 获取父节点实体
			menu.setParent(this.getMenu(menu.getParent().getId()));

			// String oldParentIds = menu.getParentIds();

			// 设置新的父节点串
			menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

			String oldId = menu.getId();
			createNewId(menu);
			String newId = menu.getId();
			Map<String, String> map = new HashMap();
			map.put("oldId", oldId);
			map.put("newId", newId);
			roleDao.updateById(map);
			menuDao.updateById(map);
			menu.preUpdate();

			menuDao.update(menu);

			List<Menu> list = menuDao.findByParentId(oldId);
			if (list == null || list.isEmpty()) {
				return;
			}
			for (Menu child : list) {
				child.setParent(menu);
				saveMenu(child);
			}
		}
		// 如果父id和依照子id查詢到的父id一致,那視為修改
		else {
			menu.preUpdate();
			menuDao.update(menu);
		}
		// 更新子节点 parentIds
		/*
		 * Menu m = new Menu(); m.setParentIds("%," + menu.getId() + ",%"); List<Menu>
		 * list = menuDao.findByParentIdsLike(m); for (Menu e : list) {
		 * e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
		 * menuDao.updateParentIds(e); }
		 */
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	/**
	 * 根据组织ID获取该组织下或该组织的所有孩子组织的所有角色,同时返回公共的角色
	 * 
	 * @param orgId
	 * @author an48huf
	 * @date 2017年11月17日
	 * @return
	 */
	public List<Role> getRoleListForOrg(String orgId) {
		List<Role> roleListForOrg = roleDao.findRoleListForOrg(orgId);
		return roleListForOrg;
	}

	/**
	 * 根据角色ID获取已经使用该角色的用户列表
	 * 
	 * @param roleId
	 * @author an48huf
	 * @date 2017年11月17日
	 * @return
	 */
	public List<User> findUserUsedRole(String roleId) {
		if (StringUtils.isBlank(roleId)) {
			return null;
		}
		return roleDao.findUserUsedRole(roleId);
	}

	/**
	 * 根据角色id获取该角色下和该角色下的所有子角色的所有用户
	 * 
	 * @param role
	 * @return
	 */
	public Page findPageUserByParentOfficeId(Page<User> page, String officeId, List<String> includeUserTypes) {
		if (StringUtils.isBlank(officeId)) {
			return null;
		}

		page.setCount(userDao.countByParentOfficeId(officeId, includeUserTypes));
		List<User> userList = userDao.findUserByParentOfficeId(officeId, page.getPageSize() * (page.getPageNo() - 1),
				page.getPageNo() * page.getPageSize(), includeUserTypes);
		page.setList(userList);
		return page;
	}

	public List<User> findUserByParentOfficeId(String officeId) {
		return userDao.findUserByParentOfficeId(officeId, 0, 100000, UserTypeEnum.valuesToList());
	}

	/**
	 * 切换国际化语言
	 * 
	 * @param userId
	 * @param lang
	 */
	@Transactional(readOnly = false)
	public void updateLangById(String userId, String lang) {

		if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(lang)) {
			userDao.updateLangById(userId, lang);
		}
	}

	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 " + Global.getConfig("productName") + "  - Powered By http://zhilink.com\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	///////////////// Synchronized to the Activiti //////////////////

	// 已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

	/**
	 * 是需要同步Activiti数据，如果从未同步过，则同步数据。
	 */
	private static boolean isSynActivitiIndetity = true;

	public void afterPropertiesSet() throws Exception {
		// if (!Global.isSynActivitiIndetity()){
		// return;
		// }
		// if (isSynActivitiIndetity){
		// isSynActivitiIndetity = false;
		// // 同步角色数据
		// List<Group> groupList = identityService.createGroupQuery().list();
		// if (groupList.size() == 0){
		// Iterator<Role> roles = roleDao.findAllList(new Role()).iterator();
		// while(roles.hasNext()) {
		// Role role = roles.next();
		// saveActivitiGroup(role);
		// }
		// }
		// // 同步用户数据
		// List<org.activiti.engine.identity.User> userList =
		// identityService.createUserQuery().list();
		// if (userList.size() == 0){
		// Iterator<User> users = userDao.findAllList(new User()).iterator();
		// while(users.hasNext()) {
		// saveActivitiUser(users.next());
		// }
		// }
		// }

		/*
		 * //启动时检查版本sql执行 (暂时不启动) try { String
		 * sql="SELECT appName, version from sys_app_version  ;"; List<Map<String,
		 * Object>> apps= jdbcTemplate.queryForList(sql); for(Map<String, Object> app :
		 * apps){ String appName=app.get("appName").toString(); String
		 * currentVersion=app.get("version").toString();
		 * 
		 * String
		 * sqlVersionPath=this.getClass().getClassLoader().getResource("").getPath()+
		 * "updatesql"; File[] fileSqls=new File(sqlVersionPath).listFiles();
		 * List<String> newSqls=new ArrayList<String>(); for(File fileSql : fileSqls){
		 * if(fileSql.getName().compareTo(currentVersion)>0){
		 * newSqls.add(fileSql.getName()); } } Collections.sort(newSqls);
		 * Collections.sort(newSqls, new Comparator<String>() { public int
		 * compare(String s1, String s2) { return s1.compareTo(s2); } }); for(String
		 * newSql : newSqls){ System.out.println(newSql); boolean
		 * result=JdbcUtils.executeSqlFile(jdbcTemplate, "/updatesql/"+newSql);
		 * if(result){ //更新版本号
		 * jdbcTemplate.execute("update sys_app_version set version='"+newSql.replace(
		 * ".sql", "")+"', uptime=now() where appName='"+appName+"'; "); } } } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */

	}

	// private void saveActivitiGroup(Role role) {
	// if (!Global.isSynActivitiIndetity()){
	// return;
	// }
	// String groupId = role.getEnname();
	//
	// // 如果修改了英文名，则删除原Activiti角色
	// if (StringUtils.isNotBlank(role.getOldEnname()) &&
	// !role.getOldEnname().equals(role.getEnname())){
	// identityService.deleteGroup(role.getOldEnname());
	// }
	//
	// Group group =
	// identityService.createGroupQuery().groupId(groupId).singleResult();
	// if (group == null) {
	// group = identityService.newGroup(groupId);
	// }
	// group.setName(role.getName());
	// group.setType(role.getRoleType());
	// identityService.saveGroup(group);
	//
	// // 删除用户与用户组关系
	// List<org.activiti.engine.identity.User> activitiUserList =
	// identityService.createUserQuery().memberOfGroup(groupId).list();
	// for (org.activiti.engine.identity.User activitiUser : activitiUserList){
	// identityService.deleteMembership(activitiUser.getId(), groupId);
	// }
	//
	// // 创建用户与用户组关系
	// List<User> userList = findUser(new User(new Role(role.getId())));
	// for (User e : userList){
	// String userId = e.getLoginName();//ObjectUtils.toString(user.getId());
	// // 如果该用户不存在，则创建一个
	// org.activiti.engine.identity.User activitiUser =
	// identityService.createUserQuery().userId(userId).singleResult();
	// if (activitiUser == null){
	// activitiUser = identityService.newUser(userId);
	// activitiUser.setFirstName(e.getName());
	// activitiUser.setLastName(StringUtils.EMPTY);
	// activitiUser.setEmail(e.getEmail());
	// activitiUser.setPassword(StringUtils.EMPTY);
	// identityService.saveUser(activitiUser);
	// }
	// identityService.createMembership(userId, groupId);
	// }
	// }
	//
	// public void deleteActivitiGroup(Role role) {
	// if (!Global.isSynActivitiIndetity()){
	// return;
	// }
	// if(role!=null) {
	// String groupId = role.getEnname();
	// identityService.deleteGroup(groupId);
	// }
	// }
	//
	// private void saveActivitiUser(User user) {
	// if (!Global.isSynActivitiIndetity()){
	// return;
	// }
	// String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
	// org.activiti.engine.identity.User activitiUser =
	// identityService.createUserQuery().userId(userId).singleResult();
	// if (activitiUser == null) {
	// activitiUser = identityService.newUser(userId);
	// }
	// activitiUser.setFirstName(user.getName());
	// activitiUser.setLastName(StringUtils.EMPTY);
	// activitiUser.setEmail(user.getEmail());
	// activitiUser.setPassword(StringUtils.EMPTY);
	// identityService.saveUser(activitiUser);
	//
	// // 删除用户与用户组关系
	// List<Group> activitiGroups =
	// identityService.createGroupQuery().groupMember(userId).list();
	// for (Group group : activitiGroups) {
	// identityService.deleteMembership(userId, group.getId());
	// }
	// // 创建用户与用户组关系
	// for (Role role : user.getRoleList()) {
	// String groupId = role.getEnname();
	// // 如果该用户组不存在，则创建一个
	// Group group =
	// identityService.createGroupQuery().groupId(groupId).singleResult();
	// if(group == null) {
	// group = identityService.newGroup(groupId);
	// group.setName(role.getName());
	// group.setType(role.getRoleType());
	// identityService.saveGroup(group);
	// }
	// identityService.createMembership(userId, role.getEnname());
	// }
	// }

	// private void deleteActivitiUser(User user) {
	// if (!Global.isSynActivitiIndetity()){
	// return;
	// }
	// if(user!=null) {
	// String userId = user.getLoginName();//ObjectUtils.toString(user.getId());
	// identityService.deleteUser(userId);
	// }
	// }
	//
	///////////////// Synchronized to the Activiti end //////////////////

	/*
	 * public void updateMenu(Menu data,String newId){ menuDao.updateById(null);
	 * List<Menu> list =menuDao.findByParentId(data.getId());
	 * if(list==null||list.isEmpty()){ return; } data.setId(newId);
	 * data.setParentIds(null); for(Menu child:list){ String childId =
	 * child.getId(); String childNewId = "到时候加入"; updateMenu(child, childNewId);
	 * child.setParent(data); child.setId(childNewId); child.setParentIds(null);
	 * 
	 * } }
	 */
}
