package com.outer.mvpcommon.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by XML on 2018/1/28.
 * description:
 */
public abstract class BasePresenterImpl implements IBasePresenter {
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Object mView;

    /**
     * presenter和对应的view绑定
     *
     * @param mvpView 目标view
     */
    @Override
    public <V extends IBaseView> void attachView(V mvpView) {
        mView = mvpView;
    }

    /**
     * presenter与view解绑
     */
    @Override
    public void detachView() {
        mView = null;
    }

    /**
     * 判断 view是否为空
     *
     * @return 子类是否绑定view
     */
    @Override
    public boolean isAttachView() {
        return mView != null;
    }

    /**
     * 将当前请求操作放到请求容器中
     *
     * @param disposable
     */
    @Override
    public void addDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }

    /**
     * 将请求容器中所有请求取消
     */
    @Override
    public void clearDisposables() {
        mDisposable.clear();
    }

    /**
     * 返回目标view
     *
     * @return
     */
    public <V extends IBaseView> V getView() {
        return (V) mView;//TODO 后期判断下类型
    }


    /**
     * 该方法必须在子类做业务逻辑执行之前检查
     * TODO 后期用的时候强调一下
     *
     * @return
     */
    public boolean isViewAlive() {
        return isAttachView();
    }


}
