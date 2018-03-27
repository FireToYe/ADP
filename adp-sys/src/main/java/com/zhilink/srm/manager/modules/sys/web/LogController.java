/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.manager.common.utils.I18n;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Log;
import com.zhilink.srm.manager.modules.sys.service.LogService;
import com.zhilink.srm.manager.modules.sys.thread.LogQueueThread;
import com.zhilink.srm.manager.modules.sys.thread.LogWriteThread;
import com.zhilink.srm.manager.modules.sys.thread.LogWriteThreadState;

/**
 * 日志Controller
 * @author jaray
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController implements InitializingBean{
	@Autowired
	private LogWriteThread logWriteThread;
	
	@Autowired
	private LogQueueThread logQueueThread;
	@Autowired
	private LogService logService;
	
	private String logState = "2";
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {"list", ""})
	public String list(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), log); 
        model.addAttribute("page", page);
		return "modules/sys/logList";
	}
	@RequiresPermissions("sys:log:edit")
	@RequestMapping(value = "changeStateSave")
	public String changeState(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String state = (String) request.getParameter("logState");
/*		if(logState.equals(state)){
			if("true".equals(state)){
				addMessage(redirectAttributes, "启动日志管理成功");
			}else{
				addMessage(redirectAttributes, "关闭日志管理成功");
			}
			return "redirect:"+Global.getAdminPath()+"/sys/log/changeState/?repage";
		}
		if("true".equals(state)){
			logWriteThread.startThread();
			this.logState=state;
			addMessage(redirectAttributes, "启动日志管理成功");
		}else if("false".equals(state)){
			logWriteThread.waitThread();
			this.logState=state;
			addMessage(redirectAttributes, "关闭日志管理成功");
		}*/
		if("1".equals(state)){
			logWriteThread.startThread();
			logQueueThread.startThread();
			this.logState=state;
		}else if("2".equals(state)){
			logWriteThread.waitThread();
			logQueueThread.startThread();
			this.logState=state;
		}else if("3".equals(state)){
			logWriteThread.startThread();
			logQueueThread.waitThread();
			this.logState=state;
		}else if("0".equals(state)){
			logWriteThread.waitThread();
			logQueueThread.waitThread();
			this.logState=state;
		}
		String i18nTip = I18n.i18nMessage("log.modifyStatusTip");
		addMessage(redirectAttributes, i18nTip);
		return "redirect:"+Global.getAdminPath()+"/sys/log/changeState/?repage";
	}
	@RequiresPermissions("sys:log:edit")
	@RequestMapping(value = "changeState")
	public String changeState(Model model) {
		model.addAttribute("logState", logState);
		return "modules/sys/logState";
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if("1".equals(logState)){
			logWriteThread.startThread();
			logQueueThread.startThread();
			logger.info("所有日志线程启动");
		}else if("2".equals(logState)){
			logWriteThread.waitThread();
			logQueueThread.startThread();
			logger.info("仅业务日志启动");
		}else if("3".equals(logState)){
			logWriteThread.startThread();
			logQueueThread.waitThread();
			logger.info("仅操作日志启动");
		}else if("0".equals(logState)){
			logWriteThread.waitThread();
			logQueueThread.waitThread();
		}
	}

}
