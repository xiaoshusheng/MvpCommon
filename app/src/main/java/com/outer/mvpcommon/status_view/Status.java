package com.outer.mvpcommon.status_view;

/**
 * Created by XML on 2018/3/21.
 * Description
 * 用于控制不同状态视图的显隐性常量类
 */
public class Status {
    //加载成功 隐藏NoNetworkView
    public static final int STATUS_NO_NETWORK_HIDE = 0;
    //网络没有连接显示NoNetworkView
    public static final int STATUS_NO_NETWORK_SHOW = 1;

    //token 恢复隐藏视图
    public static final int STATUS_TOKEN_FAIL_HIDE = 0;
    //token 失效显示视图
    public static final int STATUS_TOKEN_FAIL_SHOW = 1;
}
