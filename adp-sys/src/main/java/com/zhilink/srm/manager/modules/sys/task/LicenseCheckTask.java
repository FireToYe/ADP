package com.zhilink.srm.manager.modules.sys.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.manager.modules.sys.entity.SysMessage;
import com.zhilink.srm.manager.modules.sys.entity.SysMessageSend;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.enums.UserTypeEnum;
import com.zhilink.srm.manager.modules.sys.service.SysMessageSendService;
import com.zhilink.srm.manager.modules.sys.service.SysMessageService;
import com.zhilink.srm.manager.modules.sys.service.SystemService;

import de.schlichtherle.license.LicenseContent;

/***
 * 
 * @ClassName: LicenseCheckTask
 * @Description: License检查
 * @author Administrator
 * @date 2017年11月2日
 *
 */

  @Service
  
  @Lazy(false)
 
public class LicenseCheckTask {

	private static Logger log = LoggerFactory.getLogger(LicenseCheckTask.class);
	private final static int WARNDAY = 30;
	@Autowired
	private SystemService systemService;

	@Autowired
	private SysMessageSendService sysMessageSendService;

	@Autowired
	private SysMessageService SysMessageService;

	@Description("检查license信息")
	@Scheduled(cron = "0 15 1 * * ?")
	private void licenseCheck() {

		log.info("============= licenseCheck start=====================");
		LicenseContent licenseContent = Global.getLicenseContent();
		if (licenseContent != null) {
			// license结束时间
			Date notAfter = licenseContent.getNotAfter();
			if (notAfter == null) {
				return;
			}
			Date curDate = new Date();
			long  distanceOfTwoDate =  (notAfter.getTime() - curDate.getTime()) / (1000 * 3600 * 24);
			// 30天内站内信提示
			if (distanceOfTwoDate > 0 && distanceOfTwoDate < WARNDAY) {
				// System.err.println(licenseContent.toString());
				String content = "你的license许可证还有" + distanceOfTwoDate + "天过期，请及时激活！";
				String title = "你的license许可证即将过期";
				sendMessage(title, content);
			}
			// license过期
			if (distanceOfTwoDate < 0) {
				String content = "你的license许可证已经过期，请尽快激活！";
				String title = "你的license许可证已经过期";
				sendMessage(title, content);
			}
			log.info("============= licenseCheck end=====================");
		}
	}

	private void sendMessage(String title, String content) {
		User user = new User();
		user.setUserType(UserTypeEnum.SYSTEMUSER.getValue());
		List<User> userList = systemService.findUserNoDataScope(user);

		if (userList != null && !userList.isEmpty()) {

			SysMessageSend sysMessageSend = new SysMessageSend();
			sysMessageSend.setContent(content);
			sysMessageSend.setTitle(title);
			User creator = new User();
			creator.setId("1");
			sysMessageSend.setCreateBy(creator);
			StringBuilder sbf = new StringBuilder();
			for (User receiver : userList) {
				sbf.append(receiver.getId());
				sbf.append(",");
			}
			sysMessageSend.setReceivers(sbf.toString());
			sysMessageSend.setTarget("3");
			sysMessageSend.setUpdateBy(creator);
			sysMessageSend.setIsNewRecord(true);
			sysMessageSendService.save(sysMessageSend);

			for (User receiver : userList) {
				SysMessage sysMessage = new SysMessage();
				sysMessage.setAccepterId(receiver.getId());
				sysMessage.setTitle(title);
				sysMessage.setContent(content);
				sysMessage.setSourceMessageId(sysMessageSend.getId());
				sysMessage.setType(sysMessageSend.getType());
				sysMessage.setCreateBy(sysMessageSend.getCreateBy());
				sysMessage.setUpdateBy(sysMessageSend.getUpdateBy());
				sysMessage.setDelStatus("0");

				sysMessage.setIsNewRecord(true);
				SysMessageService.save(sysMessage);
			}

		}
	}
}
