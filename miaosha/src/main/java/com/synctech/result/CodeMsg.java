package com.synctech.result;

public class CodeMsg {

	private int code;
	private String msg;
	
	public static final CodeMsg SUCCESS = new CodeMsg(0,"success");
	public static final CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
	public static final CodeMsg REQUEST_ILLEGAL = new CodeMsg(500101,"请求非法");
	public static final CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500102,"访问太频繁！");
	
	public static final CodeMsg SESSION_ERROR = new CodeMsg(500200,"session失效");
	public static final CodeMsg PASSWORD_EMPTY = new CodeMsg(500201,"密码不能为空");
	public static final CodeMsg MOBILE_EMPTY = new CodeMsg(500202,"手机号不能为空");
	public static final CodeMsg MOBILE_ERROR = new CodeMsg(500203,"手机号格式错误");
	public static final CodeMsg PASSWORD_ERROR = new CodeMsg(500204,"密码不正确");
	public static final CodeMsg MOBILE_NOT_EXISTS = new CodeMsg(500205,"手机号不存在");
	public static final CodeMsg BIND_ERROR = new CodeMsg(500206,"%s");
	
	public static final CodeMsg MIAOSHA_OVER = new CodeMsg(500300,"秒杀活动已结束");
	public static final CodeMsg REPEAT_MIAOSHA = new CodeMsg(500301,"不能重复秒杀");
	public static final CodeMsg MIAOSHA_FAIL = new CodeMsg(500302,"秒杀失败");
	public static final CodeMsg VERIFYCODE_ERROR = new CodeMsg(500303,"验证码错误");
	public static final CodeMsg VERIFYCODE_EMPTY = new CodeMsg(500304,"验证码不能为空");
	
	public static final CodeMsg ORDER_NOT_EXISTS = new CodeMsg(500400,"订单不存在");
	
	
	
	public CodeMsg fillArgs(Object... objects){
		int code = this.code;
		String message = String.format(msg, objects);
		return new CodeMsg(code, message);
		
	}
	
	public CodeMsg(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
	
	
}
