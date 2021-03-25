package com.zxkj.common.web;

/**
 * 返回状态信息枚举
 */
public enum RespCodeEnum {

	/** 处理成功 */
	SUCCESS_200(200, "处理成功"),

	/** 处理失败*/
	ERR_9999(9999,"系统异常");

	private RespCodeEnum(int respCode,String respDesc){
		this.respCode = respCode;
		this.respDesc = respDesc;
	}

	private int respCode;
	private String respDesc;

	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

}
