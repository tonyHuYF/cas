package com.tony.cas.error;

/**
 * 错误类型
 */
public class Error {

    public static final Error INTERNAL_SERVER_ERROR = new Error(40000,"服务器内部错误");
    public static final Error UNAUTHORIZED_ACCESS = new Error(40001,"当前用户未登录");
    public static final Error FIELD_NOTNULL = new Error(40002,"不能为空");
    public static final Error PRIMARY_KEY_ERROR = new Error(40003,"主键重复错误");
    public static final Error LENGTH_ERROR = new Error(40004,"字段超长");
    public static final Error FORMAT_ERROR = new Error(40005,"字段格式错误");
    public static final Error NOT_FOUND_ERROR = new Error(40006,"数据未找到");
    public static final Error MOBILE_EXISTS= new Error(40007,"手机号码已存在");
    public static final Error VERIFICATION_CODE_EXPIRE= new Error(40008,"验证码已过期，请重新获取");
    public static final Error VERIFICATION_CODE_WRONG= new Error(40009,"验证码错误");
    public static final Error METHOD_ARGS_ERROR= new Error(40010,"参数错误");
    public static final Error HTTP_ERROR= new Error(40011,"请求错误");
    public static final Error UNAUTHORIZED_DATA_ERROR= new Error(40012,"无权访问该资源");
    public static final Error BLACK_PLAYER= new Error(40013,"黑名单用户");
    public static final Error COMMON_ERROR= new Error(40014,"发生错误");
    public static final Error METHOD_NOT_SUPPORT= new Error(40015,"访问路径错误，请检查URL和METHOD TYPE是否正确");
    public static final Error CACHE_SETTING_ERROR= new Error(40016,"缓存配置错误，请检查");
    public static final Error CACHE_INCREMENT_ERROR= new Error(40017,"缓存increment服务只支持redis，请检查缓存配置");
    public static final Error SECRET_TYPE_ERROR = new Error(40018,"加密类型配置错误，请修改成MD5或者RSA");
    public static final Error RSA_DECRYPT_ERROR = new Error(40019, "RSA解密失败，请检查您的加密内容");
    public static final Error READ_FILE_STREAM_ERROR = new Error(40020, "读取文件流错误");
    public static final Error ACCESS_TOKEN_ERROR = new Error(40021,"登录凭证失效，请重新登录。");
    private int code;
    private String message;

    public Error() {
    }

    public Error(int code, String message){
        this.code = code;
        this.message = message ;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
