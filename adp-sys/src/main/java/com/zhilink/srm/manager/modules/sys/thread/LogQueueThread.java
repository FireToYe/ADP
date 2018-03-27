package com.zhilink.srm.manager.modules.sys.thread;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.Exceptions;
import com.zhilink.manager.framework.common.utils.SpringContextHolder;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.utils.Threads;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.BaseEntity;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.manager.modules.sys.dao.LogDao;
import com.zhilink.srm.manager.modules.sys.dao.MenuDao;
import com.zhilink.srm.manager.modules.sys.entity.Log;
import com.zhilink.srm.manager.modules.sys.entity.LogAndHandlerAndEx;
import com.zhilink.srm.manager.modules.sys.entity.Menu;


/**
 * 日志工作线程
 * @author chrisyeye
 * @version 2017-09-18
 */
@Component
public class LogQueueThread implements InitializingBean{
	private static final int THREAD_SIZE = 5;
	/**
	 * 日志记录对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
	
	private static final int QUEUE_MAX_SIZE =(int) (0.8*Integer.MAX_VALUE);
	
	private volatile LogWriteThreadState state = LogWriteThreadState.STATE_STARTED;
	/**
	 * 日志队列
	 */
	private LinkedBlockingQueue<Log> queue = new LinkedBlockingQueue<Log>(QUEUE_MAX_SIZE);
	/**
	 * 当日志队列满时，则开启线程去进行阻塞队列
	 */
	private ExecutorService executor = Executors.newCachedThreadPool();
	@Autowired
	private LogDao logDao;
	@Autowired
	private MenuDao menuDao;

	/**
	 * 线程变为等待状态变为等待
	 */
	public synchronized void waitThread(){
		state=LogWriteThreadState.STATE_READY;
		queue.clear();
	}
	
	public synchronized void startThread(){
		state=LogWriteThreadState.STATE_STARTED;
	}
	
	
	public synchronized void endThread(){
		state=LogWriteThreadState.STATE_DESTROYED;
		Threads.gracefulShutdown(executor, 30, 60, TimeUnit.SECONDS);
	}
	/**
	 * 将日志实体类添加到日志队列中，如果超出队列大小，则开启线程阻塞
	 * @param icaApiLog
	 */
	public void insert(Log log) {
		if(state==LogWriteThreadState.STATE_STARTED){
			if(!queue.offer(log)){//offer方法 如果放入失败会返回false则开启线程去执行
				logger.info(log.toString());
			}
		};
	}
	public void setState(LogWriteThreadState stateStarted) {
		// TODO Auto-generated method stub
		this.state = state;
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	/*public String getMenuNamePath(String requestUri, String permission){
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
				if(requestUri!=null&&requestUri.contains("/api/")){
					return "外部api接口请求";
				}
				if(!requestUri.endsWith(".css")){
					return "非菜单请求";
				}
				return "";
			}
		}
		return menuNamePath;
	}*/
	
/*	private void insertLog(LogAndHandlerAndEx logAndHandlerAndEx){
		Log log =logAndHandlerAndEx.getLog();
		Object handler =logAndHandlerAndEx.getHandler();
		Exception ex = logAndHandlerAndEx.getEx();
		if (StringUtils.isBlank(log.getTitle())){
			String permission = "";
			if (handler instanceof HandlerMethod){
				Method m = ((HandlerMethod)handler).getMethod();
				RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
				permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
			}
			log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
		}
		if(logAndHandlerAndEx.getModel()!=null){
			Map<String,Object> model = logAndHandlerAndEx.getModel();
			if(model.containsKey("page")){
				if(model.get("page") instanceof Page){
					model.put("page",((Page)model.get("page")).getList());
				}
			}
			Set<String> set =model.keySet();
			String bean = "";
			for(String key:set){
				if(key.startsWith("org.springframework.validation.BindingResult.")&&!key.endsWith("Global")){
					bean =key.split("org.springframework.validation.BindingResult.")[1];
					break;
				}
			}
			model.remove("org.springframework.validation.BindingResult."+bean);
			model.remove("Global");
			model.remove("org.springframework.validation.BindingResult.page");
			model.remove("org.springframework.validation.BindingResult.Global");
			model.remove("menuTree");
			model.remove("genSchedule");
			
			log.setResult(JSON.toJSONString(model,true).replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n")
					 .replaceAll("\\\\t", "\t"));
		}
		// 如果有异常，设置异常信息
		log.setException(Exceptions.getStackTraceAsString(ex));
		// 如果无标题并无异常日志，则不保存信息
		if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())){
			return;
		}
		// 保存日志信息
		log.preInsert();
		try{
			logDao.insert(log);
		}catch(Exception e){
			logger.error(e.getMessage());
			logger.info(log.toString());
		}
	}*/
	
	private class LogThread implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			while(true){
				try {
					if(state==LogWriteThreadState.STATE_DESTROYED){//如果线程标志为STATE_DESTROYED状态，代表结束线程，不可重启状态。
						logger.info("系统日志工作线程已经关闭");
						return;
					}
					Log log = queue.take();
					insertLog(log);
					logger.debug("当前业务日志工作队列大小为：{},处理线程：{}",queue.size(),Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	private void insertLog(Log log){
		try{
			log.setLogLevel("2");
			logDao.insert(log);
		}catch(Exception e){
			logger.error(e.getMessage());
			logger.info(log.toString());
		}
	}
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		for(int i=0;i<THREAD_SIZE;i++){
			executor.execute(new LogThread());
		}
		executor.shutdown();
	}
}
