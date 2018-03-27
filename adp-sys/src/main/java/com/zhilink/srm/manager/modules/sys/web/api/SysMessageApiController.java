package com.zhilink.srm.manager.modules.sys.web.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.SysMessage;
import com.zhilink.srm.manager.modules.sys.service.SysMessageService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/***
* @ClassName: SysMessageApiController  
* @Description: 站内消息管理
* @author an48huf  
* @date 2017年11月29日  
*
 */
@Controller
@RequestMapping(value = "${apiPath}/message")
public class SysMessageApiController extends BaseController {

	@Autowired
	private SysMessageService sysMessageService;

	@RequestMapping(value = "list")
	public @ResponseBody ResultModel list(Integer pageNumber, Integer pageSize, String status, String keyword,
			HttpServletRequest request, HttpServletResponse response) {

		ResultModel resultModel = new ResultModel();
		SysMessage sysMessage = new SysMessage();
		if (pageNumber != null) {
			sysMessage.getPage().setPageNo(pageNumber);
		}
		if (pageSize != null) {
			sysMessage.getPage().setPageSize(pageSize);
		}
		if (StringUtils.isNotBlank(status)) {
			sysMessage.setDelStatus(status);
		}
		if (StringUtils.isNotBlank(keyword)) {
			sysMessage.setTitle(keyword);
		}
		if (!StringUtils.isNotBlank(sysMessage.getAccepterId()) || sysMessage.getAccepterId() == null
				|| "".equals(sysMessage.getAccepterId())) {
			sysMessage.setAccepterId(UserUtils.getUser().getId());
		}
		Page<SysMessage> page = sysMessageService.findPage(sysMessage.getPage(), sysMessage);
		resultModel.setBody(page);

		return resultModel;
	}

	// 获取当前用户的消息数量

	@RequestMapping(value = "notifyNum")
	public @ResponseBody ResultModel notifyNum(String status) {

		ResultModel resultModel = new ResultModel();
		String userId = UserUtils.getUser().getId();
		int num = sysMessageService.notifyNum(userId, status);
		Map<String, Integer> simplyMap = new HashMap<String, Integer>();
		simplyMap.put("num", num);
		resultModel.setBody(simplyMap);

		return resultModel;
	}

	// 点击查看消息时，更新消息状态
	@RequestMapping(value = "updateStatus")
	public @ResponseBody ResultModel delStatus(String id) {
		ResultModel resultModel = new ResultModel();
		SysMessage sysMessage = new SysMessage();
		if (StringUtils.isNotBlank(id)) {
			sysMessage.setId(id);
		}

		sysMessage.setDelStatus("1");// 已读
		sysMessage.setAccepterId(UserUtils.getUser().getId());

		sysMessageService.updateStatus(sysMessage);

		return resultModel;

	}

	@RequestMapping(value = "detail")
	public @ResponseBody ResultModel form(SysMessage sysMessage) {

		ResultModel resultModel = new ResultModel();
		if (StringUtils.isBlank(sysMessage.getId())) {
			resultModel.setErrorCode("");
			resultModel.setErrorMsg(I18n.i18nMessage("api.inputIDFull"));
			return resultModel;
		}
		SysMessage sysMessage2 = sysMessageService.get(sysMessage.getId());
		if ("0".equals(sysMessage2.getDelStatus())) {
			sysMessage2.setDelStatus("1");
			sysMessageService.save(sysMessage2);
		}

		resultModel.setBody(sysMessage2);
		return resultModel;
	}

	@RequestMapping(value = "delete")
	public @ResponseBody ResultModel delete(SysMessage sysMessage) {

		ResultModel resultModel = new ResultModel();
		if (StringUtils.isBlank(sysMessage.getId())) {
			resultModel.setErrorCode("");
			resultModel.setErrorMsg(I18n.i18nMessage("api.inputIDFull"));
			return resultModel;
		}

		sysMessageService.delete(sysMessage);
		return resultModel;
	}

}