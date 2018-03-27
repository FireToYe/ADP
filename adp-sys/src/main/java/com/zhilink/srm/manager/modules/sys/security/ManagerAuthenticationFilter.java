package com.zhilink.srm.manager.modules.sys.security;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.framework.common.api.Messages;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.enums.UserTypeEnum;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

/***
 * @ClassName: ManagerAuthenticationFilter
 * @Description: srm前端进行权限配置的权限过滤
 * @author an48huf
 * @date 2017年11月17日
 *
 */

public class ManagerAuthenticationFilter extends AccessControlFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {

		// 验证是否是管理员
		User user = UserUtils.getUser();
		if (user == null) {
			String errorCode = Messages.ACCESS_EXCEPTION_CODE;
			String errorMsg = Messages.ACCESS_EXCEPTION_MSG;
			out(response, errorCode, errorMsg);
			return false;
		}
		if (StringUtils.isBlank(user.getUserType()) || UserTypeEnum.GENERALUSER.getValue().equals(user.getUserType())) {
			String errorCode = "-1";
			String errorMsg = "非供应商管理员，没有进行该操作的权限";
			out(response, errorCode, errorMsg);
			return false;
		}
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	private void out(ServletResponse response, String errorCode, String errorMsg) {
		PrintWriter out = null;
		try {
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("status", 0);
			attributes.put("errorCode", errorCode);
			attributes.put("errorMsg", errorMsg);
			Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
			resultMap.put("head", attributes);
			response.setCharacterEncoding("UTF-8");// 设置编码
			response.setContentType("application/json");// 设置返回类型
			out = response.getWriter();
			out.println(JSONObject.toJSONString(resultMap));// 输出
		} catch (Exception e) {

		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}
		}
	}

}
