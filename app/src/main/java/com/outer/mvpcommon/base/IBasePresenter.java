package com.outer.mvpcommon.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by XML on 2018/1/28.
 * description:
 * Presenter基类，主要的提供绑定和解绑view
 */
public interface IBasePresenter {
    /**
     * 所有子类必须实现的逻辑开始入口
     */
    void start();

    /**
     * presenter和对应的view绑定
     *
     * @param mvpView 目标view
     */
    <V extends IBaseView> void attachView(V mvpView);

    /**
     * presenter与view解绑
     */
    void detachView();

    /**
     * 判断 view是否为空
     *
     * @return 子类是否绑定view
     */
    boolean isAttachView();

    /**
     * 将当前请求操作放到请求容器中
     *
     * @param disposable
     */
    void addDisposable(Disposable disposable);

    /**
     * 将请求容器中所有请求取消
     */
    void clearDisposables();
}
