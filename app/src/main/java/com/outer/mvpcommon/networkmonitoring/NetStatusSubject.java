package com.outer.mvpcommon.networkmonitoring;

/**
 * Created by XML on 2018/3/14.
 * Description
 * 使用观察者模式Observer实现对Android网络状态的监听
 * 1.监听网络变化的订阅者
 */
public interface NetStatusSubject {
    /**
     * 注册观察者
     *
     * @param observer
     */
    void registerNetStatusObserver(NetStatusObserver observer);

    /**
     * 移除观察者
     *
     * @param observer
     */
    void unregisterNetStatusObserver(NetStatusObserver observer);

    /**
     * 状态更新通知
     *
     * @param type
     */
    void notifyNetObserver(int type);
}
