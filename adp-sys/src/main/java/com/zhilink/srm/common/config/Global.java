/**
 * 
 */
package com.zhilink.srm.common.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.core.io.DefaultResourceLoader;

import com.google.common.collect.Maps;
import com.zhilink.manager.framework.common.utils.PropertiesLoader;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.manager.modules.sys.verify.VerifyLicense;

import de.schlichtherle.license.LicenseContent;

/**
 * 全局配置类
 * 
 * @author jaray
 * 
 */
public class Global {

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();
	public static final String SAVED_REQUEST_KEY = "shiroSavedRequest";
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("zhilink.properties");


	/**
	 * 获取License信息
	 * 
	 * @return
	 */
	public static LicenseContent getLicenseContent() {
		return VerifyLicense.getLicenseContent();
	}

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";

	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}

	/**
	 * 获取配置
	 * 
	 * @see ${fns:getConfig('adminPath')}
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null) {
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}

	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}

	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}

	/**
	 * 获取app版本路径
	 */
	public static String getWmsAppPath() {
		return getConfig("wmsAppPath");
	}

	/**
	 * 获取app版本下载路径
	 */
	public static String getWmsAppDownload() {
		return getConfig("wmsAppDownload");
	}

	/**
	 * 获取管理端根路径
	 */
	public static String getApiPath() {
		return getConfig("apiPath");
	}

	public static boolean isApiRequest(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		if (url.indexOf(getApiPath()) > -1) {
			return true;
		} else {
			SavedRequest sv = WebUtils.getSavedRequest(request);
			if (sv != null) {
				if (sv.getRequestUrl().indexOf(getApiPath()) > -1) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}
	}

	public static void removeSavedReq() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		if (null != session) {
			session.removeAttribute(SAVED_REQUEST_KEY);
		}
	}

	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}

	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 页面获取常量
	 * 
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// 异常代表无配置，这里什么也不做
		}
		return null;
	}

	/**
	 * 获取上传文件的根目录
	 * 
	 * @return
	 */
	public static String getUserfilesBaseDir() {
		String dir = getConfig("userfiles.basedir");
		if (StringUtils.isBlank(dir)) {
			try {
				dir = "";// ServletContextFactory.getServletContext().getRealPath("/");
				// TODO fix
			} catch (Exception e) {
				return "";
			}
		}
		if (!dir.endsWith("/")&&dir.length()>0) {
			dir += "/";
		}
		// System.out.println("userfiles.basedir: " + dir);
		return dir;
	}

	/**
	 * 获取工程路径
	 * 
	 * @return
	 */
	public static String getProjectPath() {
		// 如果配置了工程路径，则直接返回，否则自动获取。
		String projectPath = Global.getConfig("projectPath");
		if (StringUtils.isNotBlank(projectPath)) {
			return projectPath;
		}
		try {
			File file = new DefaultResourceLoader().getResource("").getFile();
			if (file != null) {
				while (true) {
					File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
					if (f == null || f.exists()) {
						break;
					}
					if (file.getParentFile() != null) {
						file = file.getParentFile();
					} else {
						break;
					}
				}
				projectPath = file.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return projectPath;
	}

}
