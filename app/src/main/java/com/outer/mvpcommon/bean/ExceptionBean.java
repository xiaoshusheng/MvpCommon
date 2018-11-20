package com.outer.mvpcommon.bean;

/**
 * Created by YZK on 2018/9/28.
 * description:
 * type              枚举类型错误码类型
 * code              错误码值
 * msg               对应的错误信息
 * data              其他数据
 */
public class ExceptionBean {
    public static final int NO_CODE = -1;
    private ExceptionEnum type;
    private int code;
    private String msg;


    public ExceptionEnum getType() {
        return type;
    }

    public void setType(ExceptionEnum type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "ExceptionBean{" +
                "type=" + type +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
