package com.outer.mvpcommon.status_view;

/**
 * Created by XML on 2018/3/21.
 * Description
 * 状态视图(无网络,无数据,错误,加载中)中控件点击事件的回调接口
 */
public interface IStatusViewItemCallback {
    /**
     * 无网络视图中点击重试btn的点击事件回调
     *
     * @param networkAvailable 网络是否可用
     */
    void onNoNetworkBtnRetryClick(boolean networkAvailable);
}
