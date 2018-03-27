package com.zhilink.adp.client.exception;


/**
 * 
 * @author jaray
 *
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 2139177956508243331L;
	private int status = 0;
	private String errorCode   ;
	private String errorMsg  ;
	private String extention = "";

	public ApiException(String errorMsg ) {
		super( errorMsg);
		this.errorMsg=errorMsg;
	}
	public ApiException () {
		super(  );
	}
	public ApiException(String errorCode, String errorMsg) {
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
