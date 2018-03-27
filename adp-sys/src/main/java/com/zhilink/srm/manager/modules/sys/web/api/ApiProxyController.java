/**
 * 
 */
package com.zhilink.srm.manager.modules.sys.web.api;


import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.exception.CommonException;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.service.ApiProxyService;

/**
 * 登录Controller
 * 
 * @author jaray
 * 
 */
@Controller
public class ApiProxyController extends BaseController {
	@Autowired
	private ApiProxyService apiProxyService;

	@RequestMapping(value = "${apiPath}/proxy/{serviceCode}")
	@ResponseBody
	public ResultModel Apiroxy(@PathVariable("serviceCode") String serviceCode, @RequestHeader HttpHeaders headers,
			@RequestBody String bodys, HttpServletRequest request)  {
		try {
			if (StringUtils.isEmpty(bodys)) {
				throw new CommonException(I18n.i18nMessage("api.serviceCode"));
			}
			ResultModel result = new ResultModel();
			result.setBody(this.apiProxyService.doPost(serviceCode, bodys));
			return result;
		} catch (Exception ex) {//
		   ex.printStackTrace();
		   throw new CommonException(ex.getMessage());
		}

	}

}
