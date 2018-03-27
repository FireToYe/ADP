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

public class DefaultExceptionHandler implements HandlerExceptionResolver {
	private static Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);
    
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		if (ex instanceof CommonException) {
			ModelAndView mv = new ModelAndView();
			/* 使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常 */
			FastJsonJsonView view = new FastJsonJsonView();
			Map<String, Object> attributes = new HashMap<String, Object>();
			CommonException e = (CommonException) ex;
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