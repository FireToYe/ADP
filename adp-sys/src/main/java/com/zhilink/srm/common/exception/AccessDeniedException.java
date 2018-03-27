package com.zhilink.srm.common.exception;

import com.zhilink.manager.framework.common.api.Messages;

/**
 * 用户认证异常.
 * 
 * @author jaray
 *
 */
public class AccessDeniedException extends CommonException {

	private static final long serialVersionUID = 2139177956508243331L;

	public AccessDeniedException() {
		super(Messages.ACCESS_EXCEPTION_CODE, Messages.ACCESS_EXCEPTION_MSG);
	}

	public AccessDeniedException(String erroCode, String errorMsg) {
		super(erroCode, errorMsg);
	}

}
