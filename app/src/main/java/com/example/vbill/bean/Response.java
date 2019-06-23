package com.example.vbill.bean;

public class Response {
	public int statusCode;
	public Object data;
	public String errorCode;
	public String errorMsg;
	public Response(int statusCode, Object data) {
		super();
		this.statusCode = statusCode;
		this.data = data;
	}
	public Response(String errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public Response(int statusCode, Object data, String errorCode, String errorMsg) {
		this.statusCode = statusCode;
		this.data = data;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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
}
