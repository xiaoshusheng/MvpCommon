package com.outer.mvpcommon.base;


import com.outer.mvpcommon.bean.ExceptionBean;

/**
 * Created by XZL on 2018/9/28.
 * description:
 */
public interface IBaseView {


    /**
     * 显示进度对话框
     *
     * @param msg        进度条加载内容
     * @param cancelable
     */
    void showLoadingDialog(String msg, boolean cancelable);

    /**
     * 取消进度对话框
     */
    void dismissLoadingDialog();

    /**
     * 当首次进入没有网络时调用的方法
     */
    void onFirstEnterNoNetwork();

    /**
     * 手机系统网络异常回调方法
     *
     * @param noNetwork true 无网络,false 有网络
     */
    void onNoNetwork(boolean noNetwork);

    /**
     * 线上服务器出现的网络模块异常
     */
    void onServerNetException();

    /**
     * 特殊情况下的失败处理
     *
     * @param exceptionBean 异常处理的统一类
     * @return true 则表示调用子类的 false 表示调用父类的默认实现,子类默认空实现
     */
    void onFail(ExceptionBean exceptionBean);

    /**
     * 数据为空的回调方法
     */
    void onEmpty();

    /**
     * 适配5.0及以上状态栏沉浸式(里面设置颜色0xfff08519, ff不能删去必须是不透明的)
     */
    int setStatusBarColor();
}
