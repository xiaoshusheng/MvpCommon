package com.outer.mvpcommon.bean;

/**
 * Created by YZL on 2018/9/28.
 * description:
 * 网络封装框架下错误类型的枚举，以后可以扩展
 */
public enum ExceptionEnum {
    /**
     * 重新登陆
     */
    RELOGIN,
    /**
     * 已经注册但为其他角色
     */
    REGISTER_OTHER_ROLE,
    /**
     * 验证码错误
     */
    VERIFICATIONCODE_ERROR,
    /**
     * 链接超时
     */
    TIME_OUT,
    /**
     * 账户已禁用
     */
    USER_FORBIDDEN,
    /**
     * 其他问题
     */
    OTHER
}
