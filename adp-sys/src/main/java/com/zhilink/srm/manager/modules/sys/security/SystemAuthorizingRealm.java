/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.security;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.xerces.util.SynchronizedSymbolTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhilink.manager.framework.common.security.Digests;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.Encodes;
import com.zhilink.manager.framework.common.utils.SpringContextHolder;
import com.zhilink.manager.framework.common.web.Servlets;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.servlet.ValidateCodeServlet;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.entity.Role;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.LogUtils;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;
import com.zhilink.srm.manager.modules.sys.web.LoginController;

/**
 * 系统安全认证实现类
 * 
 * @author jaray
 * 
 */
/* @Service */
// @DependsOn({"userDao","roleDao","menuDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private SystemService systemService;
	private List<String> exclusions;
	protected static final String WILDCARD_TOKEN = "*";
	protected static final String PART_DIVIDER_TOKEN = ":";

	public List<String> getExclusions() {
		return exclusions;
	}

	public void setExclusions(List<String> exclusions) {
		this.exclusions = exclusions;
	}

	public SystemAuthorizingRealm() {
		this.setCachingEnabled(false);
	}

	/**
	 * 认证回调函数, 登录时调用
	 */

	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		int activeSessionSize = getSystemService().getSessionDao().getActiveSessions(false).size();
		if (logger.isDebugEnabled()) {
			logger.debug("login submit, active session size: {}, username: {}", activeSessionSize, token.getUsername());
		}

		// 校验登录错误次数,取代验证码
		if (LoginController.isValidateCodeLogin(token.getUsername(), false, false)) {
			Session session = UserUtils.getSession();
			Map<String, Long> loginFailMap = (Map<String, Long>) CacheUtils.get("loginFailMap");
			Long unlockMillis = loginFailMap.get(token.getUsername() + ":unlockMillis");
			if (unlockMillis != null) {
				long lastTime = unlockMillis - System.currentTimeMillis();
				if (lastTime > 0) {
					throw new AuthenticationException("msg:登录密码输入错误次数超过上限, 请等待" + lastTime / 1000 + "秒.");
				}
			}
			/*
			 * String code = (String)
			 * session.getAttribute(ValidateCodeServlet.VALIDATE_CODE); if
			 * (token.getCaptcha() == null ||
			 * !token.getCaptcha().toUpperCase().equals(code)) { throw new
			 * AuthenticationException("msg:验证码错误, 请重试."); }
			 */
		}

		// 校验用户名密码
		/*User user = getSystemService().getUserByLoginName(token.getUsername());
		if (user != null) {
			if (Global.NO.equals(user.getLoginFlag())) {
				throw new AuthenticationException("msg:该已帐号禁止登录.");
			}
			byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
			return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin()),
					user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
		} else {
			return null;
		}*/
		
		// 校验用户名密码
		User user = getSystemService().getUserByLoginName(token.getUsername());
		if (user != null) {
			if (Global.NO.equals(user.getLoginFlag())) {
				throw new AuthenticationException("msg:该已帐号禁止登录.");
			}
			if (token.getLoginType() == LoginType.OTHRE) {
				byte[] salt = Digests.generateSalt(SALT_SIZE);
				byte[] hashPassword = Digests.sha1(token.getUsername().getBytes(), salt, HASH_INTERATIONS);
				
				return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin()),
						Encodes.encodeHex(hashPassword), ByteSource.Util.bytes(salt), getName());
			}else{
				byte[] salt = Encodes.decodeHex(user.getPassword().substring(0, 16));
				return new SimpleAuthenticationInfo(new Principal(user, token.isMobileLogin()),
						user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
			}
			
		} else {
			return null;
		}

	}

	public Collection<String> getUserPermessions() {
		AuthorizationInfo info = getAuthorizationInfo(UserUtils.getSubject().getPrincipals());
		return info.getStringPermissions();
	}

	/**
	 * 获取权限授权信息，如果缓存中存在，则直接从缓存中获取，否则就重新获取， 登录成功后调用
	 */
	protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			return null;
		}

		AuthorizationInfo info = null;

		info = (AuthorizationInfo) UserUtils.getCache(UserUtils.CACHE_AUTH_INFO);

		if (info == null) {
			info = doGetAuthorizationInfo(principals);
			if (info != null) {
				UserUtils.putCache(UserUtils.CACHE_AUTH_INFO, info);
			}
		}

		return info;
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */

	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
			Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal,
					UserUtils.getSession());
			if (sessions.size() > 0) {
				// 如果是登录进来的，则踢出已在线用户
				if (UserUtils.getSubject().isAuthenticated()) {
					for (Session session : sessions) {
						getSystemService().getSessionDao().delete(session);
					}
				}
				// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
				else {
					UserUtils.getSubject().logout();
					throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
				}
			}
		}
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<Menu> list = UserUtils.getMenuList();
			for (Menu menu : list) {
				if (StringUtils.isNotBlank(menu.getPermission())) {
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(), ",")) {
						info.addStringPermission(permission);
					}
				}
			}
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			for (Role role : user.getRoleList()) {
				info.addRole(role.getEnname());
			}
			// 更新登录IP和时间
			getSystemService().updateUserLoginInfo(user);
			// 记录登录日志
			LogUtils.saveLog(Servlets.getRequest(), "系统登录");
			return info;
		} else {
			return null;
		}
	}

	protected void checkPermission(Permission permission, AuthorizationInfo info) {
		authorizationValidate(permission);
		super.checkPermission(permission, info);
	}

	protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
			for (Permission permission : permissions) {
				authorizationValidate(permission);
			}
		}
		return super.isPermitted(permissions, info);
	}

	public boolean isPermitted(PrincipalCollection principals, Permission permission) {
		authorizationValidate(permission);
		// 自定义权限过滤
		if (exclusions != null && !exclusions.isEmpty()) {
			for (String exclusionPer : exclusions) {
				if (StringUtils.isBlank(exclusionPer))
					continue;
				WildcardPermission exclusionPermission = new WildcardPermission(exclusionPer);
				if (exclusionPermission.implies(permission)) {
					return true;
				}
			}
		}
		return super.isPermitted(principals, permission);
	}

	protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
			for (Permission permission : permissions) {
				authorizationValidate(permission);
			}
		}
		return super.isPermittedAll(permissions, info);
	}

	/**
	 * 授权验证方法
	 * 
	 * @param permission
	 */
	private void authorizationValidate(Permission permission) {
		// 模块授权预留接口
	}

	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	// /**
	// * 清空用户关联权限认证，待下次使用时重新加载
	// */
	// public void clearCachedAuthorizationInfo(Principal principal) {
	// SimplePrincipalCollection principals = new
	// SimplePrincipalCollection(principal, getName());
	// clearCachedAuthorizationInfo(principals);
	// }

	/**
	 * 清空所有关联认证
	 * 
	 * @Deprecated 不需要清空，授权缓存保存到session中
	 */
	@Deprecated
	public void clearAllCachedAuthorizationInfo() {
		// Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		// if (cache != null) {
		// for (Object key : cache.keys()) {
		// cache.remove(key);
		// }
		// }
	}

	/**
	 * 获取系统业务对象
	 */
	public SystemService getSystemService() {
		if (systemService == null) {
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}

	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;

		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名
		private boolean mobileLogin; // 是否手机登录

		// private Map<String, Object> cacheMap;

		public Principal(User user, boolean mobileLogin) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
			this.mobileLogin = mobileLogin;
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public boolean isMobileLogin() {
			return mobileLogin;
		}

		// @JsonIgnore
		// public Map<String, Object> getCacheMap() {
		// if (cacheMap==null){
		// cacheMap = new HashMap<String, Object>();
		// }
		// return cacheMap;
		// }

		/**
		 * 获取SESSIONID
		 */
		public String getSessionid() {
			try {
				return (String) UserUtils.getSession().getId();
			} catch (Exception e) {
				return "";
			}
		}

		public String toString() {
			return id;
		}

	}
}
