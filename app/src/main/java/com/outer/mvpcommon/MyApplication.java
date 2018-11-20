package com.outer.mvpcommon;

import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.outer.mvpcommon.networkmonitoring.NetConnectionReceiver;
import com.outer.mvpcommon.networkmonitoring.NetStatusObserver;
import com.outer.mvpcommon.networkmonitoring.NetStatusSubject;
import com.outer.mvpcommon.utils.NetUtil;
import com.outer.mvpcommon.utils.Utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：create by YangZ on 2018/9/14 09:33
 * 邮箱：YangZL8023@163.com
 */

public class MyApplication extends MultiDexApplication implements NetStatusSubject {
    /**
     * 全局的上下文
     */
    private static Context mContext;
    /**
     * 主线程的handler
     */
    private static Handler mMainThreadHandler;
    /**
     * 主线程的ID
     */
    private static int mMainThreadId;

    private Map<String, String> mMemProtocalCache = new HashMap<>();
    private NetConnectionReceiver mNetConnectionReceiver;

    public Map<String, String> getMemProtocalCache() {
        return mMemProtocalCache;
    }

    protected static MyApplication mInstance;

    private int mCurrentNetType = NetUtil.NET_NO_CONNECTION;

    private List<NetStatusObserver> mObservers = new ArrayList<>();

    public static MyApplication getInstance() {
        return mInstance;
    }

    /**
     * current net connection type
     *
     * @return
     */
    public int getCurrentNetType() {
        return mCurrentNetType;
    }

    /**
     * current net connection status
     *
     * @return
     */
    public boolean isNetConnection() {
        return mCurrentNetType != NetUtil.NET_NO_CONNECTION;
    }


    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        mMainThreadHandler = new Handler();
        mMainThreadId = android.os.Process.myTid();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        super.onCreate();
        Utils.init(this);
        initBugly();

        mInstance = this;
        mCurrentNetType = NetUtil.checkNetworkType(this);
        /**
         * 注册网络变化的接受者
         */
        mNetConnectionReceiver = new NetConnectionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetConnectionReceiver, filter);

    }


    /**
     * 初始化Bugly
     */
    private void initBugly() {
        // 获取当前包名
        String packageName = mContext.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());

    }

    public static Context getContext() {
        return mContext;
    }

    //
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 下面两个方法是控制字体不跟随系统字体变化的
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 注册观察者
     *
     * @param observer
     */
    @Override
    public void registerNetStatusObserver(NetStatusObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    /**
     * 移除观察者
     *
     * @param observer
     */
    @Override
    public void unregisterNetStatusObserver(NetStatusObserver observer) {
        if (mObservers != null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    /**
     * 状态更新通知
     *
     * @param type
     */
    @Override
    public void notifyNetObserver(int type) {
        /**
         * 避免多次发送相同的网络状态
         */
        if (mCurrentNetType == type) {
            return;
        }
        mCurrentNetType = type;
        if (mObservers != null && mObservers.size() > 0) {
            for (NetStatusObserver observer : mObservers) {
                observer.updateNetStatus(type);
            }
        }

    }

}
