package com.zhilink.srm.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.zhilink.srm.common.config.Global;

public class ApiExceptionHandler implements HandlerExceptionResolver {
	private static Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {

		if (Global.isApiRequest(request)) {
			Global.removeSavedReq();
			ModelAndView mv = new ModelAndView();
			/* 使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常 */
			FastJsonJsonView view = new FastJsonJsonView();
			Map<String, Object> attributes = new HashMap<String, Object>();
			CommonException e = null;
			if (ex instanceof CommonException) {

				e = (CommonException) ex;

			} else {
				e = new CommonException(ex.getMessage());
			}
			attributes.put("status", e.getStatus());
			attributes.put("errorCode", e.getErrorCode());
			attributes.put("errorMsg", e.getErrorMsg());
			HashMap<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
			resultMap.put("head", attributes);
			view.setAttributesMap(resultMap);
			mv.setView(view);
			log.debug("异常:" + ex.getMessage(), ex);
			ex.printStackTrace();
			return mv;

		} else {

			return null;
		}

	}

}