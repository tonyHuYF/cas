package com.tony.cas.error;

/**
 * 业务错误
 */
public class BusinessError extends Error {


    public static final Error SERVICE_ID_NULL_ERROR = new Error(150001, "serviceId不能为空。");
    public static final Error ADD_CLIENT_ERROR = new Error(150002, "注册service异常，客户端添加失败。");
    public static final Error SERVICE_NULL_ERROR = new Error(150003, "service为null，客户端删除失败。");
    public static final Error DELETE_CLIENT_ERROR = new Error(150004, "删除service异常,客户端删除失败。");
    public static final Error CODE_CONFIG_ERROR = new Error(150005, "验证码配置信息错误！正确配置查看 LoginCodeEnum ");
    public static final Error USERNAME_OR_PASSWORD_ERROR = new Error(150006, "用户名或密码错误。");
    public static final Error ACCOUNT_NOT_AVAILABLE_ERROR = new Error(150007, "账号不可用,请联系管理员！");

}
