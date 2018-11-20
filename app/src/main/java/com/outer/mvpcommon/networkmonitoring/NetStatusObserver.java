package com.outer.mvpcommon.networkmonitoring;

/**
 * Created by XML on 2018/3/14.
 * Description
 * 使用观察者模式Observer实现对Android网络状态的监听
 * 1.监听网络变化的观察者
 */
public interface NetStatusObserver {

    /**
     * 通知观察者更改状态
     *
     * @param type NetUtil.NET_TYPE_2G 等
     */
    void updateNetStatus(int type);
}
