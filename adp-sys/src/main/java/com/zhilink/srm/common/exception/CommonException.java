package com.zhilink.srm.common.exception;

import com.zhilink.manager.framework.common.api.Messages;

/**
 * 用户认证异常.
 * 
 * @author jaray
 *
 */
public class CommonException extends RuntimeException {

	private static final long serialVersionUID = 2139177956508243331L;
	private int status = 0;
	private String errorCode = Messages.SERVER_EXCEPTION_CODE;
	private String errorMsg = Messages.SERVER_EXCEPTION_MSG_CN;
	private String extention = "";

	public CommonException(String errorMsg ) {
		super( errorMsg);
		this.errorMsg=errorMsg;
	}
	public CommonException () {
		super(  Messages.SERVER_EXCEPTION_MSG_CN);
	}
	public CommonException(String errorCode, String errorMsg) {
		super( errorMsg);
		this.errorMsg=errorMsg;
		this.errorCode=errorCode;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
