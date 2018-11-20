package com.outer.mvpcommon.base;

import com.outer.mvpcommon.bean.ExceptionBean;

import io.reactivex.disposables.Disposable;

/**
 * Created by YZL on 2018/9/28.
 * description:
 */
public interface IBaseCallback<T> {

    /**
     * 服务器返回响应码为0数据
     *
     * @param response 服务器返回的数据
     */
    void onSuccess(T response);

    /**
     * 特殊情况下的失败处理
     *
     * @param exceptionBean 异常处理的统一类
     * @return true 则表示调用子类的 false 表示调用父类的默认实现,子类默认空实现
     */
    boolean onFail(ExceptionBean exceptionBean);

    /**
     * 数据为空的回调方法
     */
    void onEmpty();

    /**
     * 网络异常回调方法
     *
     * @return true 则表示调用子类的 false 表示调用父类的默认实现
     */
    boolean onNoNetwork();

    /**
     * 订阅事件返回值
     *
     * @param disposable 订阅事件返回值
     */
    void onSubscribeReturn(Disposable disposable);

}
