package com.outer.mvpcommon.bean.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fjw on 2017/11/8.
 * 服务端返回数据 封装模型 data属性是数组
 */

public class ResponseData<T> extends BaseBean implements Serializable {
    /**
     * 返回结果数据结果集
     */
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}