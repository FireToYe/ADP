/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.utils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.Exceptions;
import com.zhilink.manager.framework.common.utils.SpringContextHolder;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.manager.modules.sys.dao.LogDao;
import com.zhilink.srm.manager.modules.sys.dao.MenuDao;
import com.zhilink.srm.manager.modules.sys.entity.Log;
import com.zhilink.srm.manager.modules.sys.entity.LogAndHandlerAndEx;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.thread.LogQueueThread;
import com.zhilink.srm.manager.modules.sys.thread.LogWriteThread;

/**
 * 字典工具类
 * @author jaray
 * 
 */
public class LogUtils {
	
	public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
	
	private static LogDao logDao = SpringContextHolder.getBean(LogDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static LogWriteThread logWriteThread = SpringContextHolder.getBean(LogWriteThread.class);
	private static LogQueueThread logQueueThread = SpringContextHolder.getBean(LogQueueThread.class);
	/**
	 * 日志记录对象
	 */
	protected static Logger logger = LoggerFactory.getLogger(LogUtils.class);
	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title){
		saveLog(request, null, null, title,null);
	}
	
	/**
	 * 
	 * @param request
	 * @param handler
	 * @param ex
	 * @param title
	 * @param model
	 * @deprecated 该方法已被废除，业务日志调用saveLogNew
	 */
	@Deprecated  
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title,Map<String,Object> model){
		User user = UserUtils.getUser();
		if (user != null && user.getId() != null){
			Log log = new Log();
			log.setTitle(title);
			log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
			log.setRemoteAddr(StringUtils.getRemoteAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
			Map<String,String[]>  requestMap = request.getParameterMap();
			log.setParams(requestMap);
			log.setMethod(request.getMethod());
			if(requestMap.get("appmodule")!=null&&requestMap.get("appmodule").length>0){
				log.setModules(requestMap.get("appmodule")[0]);
			}
			// 异步保存日志
			LogAndHandlerAndEx logAndHandlerAndEx =new LogAndHandlerAndEx();
			logAndHandlerAndEx.setLog(log);
			logAndHandlerAndEx.setHandler(handler);
			logAndHandlerAndEx.setEx(ex);
			logAndHandlerAndEx.setModel(model);
			logAndHandlerAndEx.setContextPath(request.getContextPath());
			log.preInsert();
			logWriteThread.insert(logAndHandlerAndEx);
		}
	}

	/**
	 * 保存业务日志
	 * @param request 传入request对象
	 * @param ex 为异常对象，如为空，传入null
	 * @param title 日志标题，必须传入
	 * @param uri 如为记录调用erp平台，则传入完整erp地址，否则传入null或"",会自动获取该方法url
	 * @param modules wms的modules标志
	 */
	public static void saveLogNew(HttpServletRequest request, Exception ex, String title,String uri,String modules,String params,String result){
		try{
			User user = UserUtils.getUser();
			if (user != null && user.getId() != null){
				Log log = new Log();
				log.setTitle(title);
				log.setModules(modules);
				log.setParams(params);
				log.setResult(result);
				log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
				if(request!=null){
					log.setRemoteAddr(StringUtils.getRemoteAddr(request));
					log.setUserAgent(request.getHeader("user-agent"));
					log.setMethod(request.getMethod());
				}
				if(uri==null||uri.equals("")){
					if(request!=null){
						log.setRequestUri(request.getRequestURI());
					}
				}else{
					log.setRequestUri(uri);
				}
				// 异步保存日志
				log.setException(Exceptions.getStackTraceAsString(ex));
				log.preInsert();
				logQueueThread.insert(log);
			}
		}catch(Exception e){
			logger.error(Exceptions.getStackTraceAsString(e));
			e.printStackTrace();
		}
	}
	/**
	 * 保存日志线程
	 */
	public static class SaveLogThread extends Thread{
		
		private Log log;
		private Object handler;
		private Exception ex;
		
		public SaveLogThread(Log log, Object handler, Exception ex){
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.handler = handler;
			this.ex = ex;
		}
		
		
		public void run() {
			// 获取日志标题
			if (StringUtils.isBlank(log.getTitle())){
				String permission = "";
				if (handler instanceof HandlerMethod){
					Method m = ((HandlerMethod)handler).getMethod();
					RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
					permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
				}
				log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
			}
			// 如果有异常，设置异常信息
			log.setException(Exceptions.getStackTraceAsString(ex));
			// 如果无标题并无异常日志，则不保存信息
			if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())){
				return;
			}
			// 保存日志信息
			log.preInsert();
			logDao.insert(log);
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	public static String getMenuNamePath(String requestUri, String permission){
		String href = StringUtils.substringAfter(requestUri, Global.getAdminPath());
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>)CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null){
			menuMap = Maps.newHashMap();
			List<Menu> menuList = menuDao.findAllList(new Menu());
			for (Menu menu : menuList){
				// 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
				String namePath = "";
				if (menu.getParentIds() != null){
					List<String> namePathList = Lists.newArrayList();
					for (String id : StringUtils.split(menu.getParentIds(), ",")){
						if (Menu.getRootId().equals(id)){
							continue; // 过滤跟节点
						}
						for (Menu m : menuList){
							if (m.getId().equals(id)){
								namePathList.add(m.getName());
								break;
							}
						}
					}
					namePathList.add(menu.getName());
					namePath = StringUtils.join(namePathList, "-");
				}
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					menuMap.put(menu.getHref(), namePath);
				}else if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						menuMap.put(p, namePath);
					}
				}
				
			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
		}
		String menuNamePath = menuMap.get(href);
		if (menuNamePath == null){
			for (String p : StringUtils.split(permission)){
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}

	
}
