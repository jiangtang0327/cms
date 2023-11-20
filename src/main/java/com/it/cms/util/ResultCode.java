package com.it.cms.util;

/**
 * 统一并自定义返回状态码，如有需求可以另外增加
 */
public enum ResultCode {
	
	/* 成功状态码 */
	SUCCESS(200, "操作成功"),
	
	/* 参数错误：10001-19999 */
	PARAM_IS_INVALID(10001, "参数无效"),
	PARAM_IS_BLANK(10002, "参数为空"),
	PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
	PARAM_NOT_COMPLETE(10004, "参数缺失"),
	
	
	/* 用户错误：20001-29999*/
	USER_NOT_LOGIN(20001, "用户未登录"),
	USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
	USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
	USER_NOT_EXIST(20004, "用户不存在"),
	USER_HAS_EXISTED(20005, "用户已存在"),
	USER_NOT_IS_VIP(20006, "用户不是VIP"),
	
	/* 业务错误：30001-39999 */
	SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "业务逻辑出现问题"),
	
	/* 系统错误：40001-49999 */
	SYSTEM_INNER_ERROR(40001, "系统内部错误，请稍后重试"),
	
	/* 数据错误：50001-599999 */
	DATA_NONE(50001, "数据未找到"),
	DATA_WRONG(50002, "数据错误"),
	DATA_EXISTED(50003, "数据已存在"),
	SLIDESHOW_NOT_EXISTED(50004, "轮播图不存在"),
	SLIDESHOW_URL_EXISTED(50005,"轮播图URL已存在"),
	USERNAME_HAS_EXISTED(50006, "用户名已存在"),
	PARENT_ID_NONE(50007, "父级ID不存在"),
	PCATEGORY_IS_INVALID(50088, "父级分类不存在"),
	CATEGORY_EXIST_ARTICLE(50009, "分类下存在文章"),
	CATEGORY_LEVEL_ERROR(50010, "栏目级别不能改动"),
	//作者账号为不存在
	AUTHOR_NOT_EXIST(50011, "作者账号不存在"),
	
	/* 接口错误：60001-69999 */
	INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
	INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
	INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
	INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
	INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
	
	/* 权限错误：70001-79999 */
	PERMISSION_NO_ACCESS(70001, "无访问权限"),

	FAILURE(99999, "操作失败");

    private Integer code;

	private String message;

	ResultCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer code() {
		return this.code;
	}

	public String message() {
		return this.message;
	}
}
