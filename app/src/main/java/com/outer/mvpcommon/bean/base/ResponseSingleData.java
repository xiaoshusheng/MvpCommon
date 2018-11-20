package com.outer.mvpcommon.bean.base;

import java.io.Serializable;

/**
 * Created by fjw on 2017/11/8.
 * 服务端返回数据 封装模型 data属性是单个对象,不是数组
 */

public class ResponseSingleData<T> extends BaseBean implements Serializable {

    /**
     * 返回结果数据结果集
     */
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}