package com.outer.mvpcommon.bean.base;

/**
 * Created by xiaomili on 2018/1/5.
 * description:
 */
public class BaseBean {
    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 0;
    /**
     * 返回结果状态码
     * 0 成功
     */
    private int code;

    /**
     * 返回结果描述
     */
    private String message;


    public static int getSuccessCode() {
        return SUCCESS_CODE;
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
