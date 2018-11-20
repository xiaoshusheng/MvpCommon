package com.outer.mvpcommon.networkmonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.outer.mvpcommon.MyApplication;
import com.outer.mvpcommon.utils.NetUtil;


/**
 * Created by XML on 2018/3/14.
 * Description
 * 网络连接状态的监听器
 */
public class NetConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int connectionType = NetUtil.checkNetworkType(context);
            /**
             * 更改网络状态
             */
            if (MyApplication.getInstance() != null) {
                MyApplication.getInstance().notifyNetObserver(connectionType);
            }
        }
    }
}
