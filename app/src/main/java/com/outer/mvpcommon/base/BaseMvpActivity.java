package com.outer.mvpcommon.base;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.outer.mvpcommon.MyApplication;
import com.outer.mvpcommon.R;
import com.outer.mvpcommon.networkmonitoring.NetStatusObserver;
import com.outer.mvpcommon.status_view.IStatusViewItemCallback;
import com.outer.mvpcommon.status_view.LoadingDialog;
import com.outer.mvpcommon.status_view.NoNetworkView;
import com.outer.mvpcommon.status_view.Status;
import com.outer.mvpcommon.utils.NetUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by XZL on 2018/9/28.
 * description:
 * mvp 中Activity基类
 */
public abstract class BaseMvpActivity<P extends IBasePresenter> extends RxAppCompatActivity
        implements IBaseView, NetStatusObserver, IStatusViewItemCallback {
    private LoadingDialog mProgressDialog;//登录进度条
    protected P mPresenter;
    private NoNetworkView mNoNetworkView;
    private TextView mNoNetworkTipPanel;
    private boolean mFinish;//关闭自己

    public abstract P createPresent();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoNetworkView = findViewById(R.id.no_network_view);
        mNoNetworkTipPanel = findViewById(R.id.no_network_tip_panel);
        MyApplication.getInstance().registerNetStatusObserver(this);
        if (!MyApplication.getInstance().isNetConnection()) {
            onFirstEnterNoNetwork();
        }
        if (mPresenter == null) {
            mPresenter = createPresent();
        }
        if (!mPresenter.isAttachView()) {
            mPresenter.attachView(this);//presenter与view断开连接
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());//子类没有设置,则使用父类默认值
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter == null) {
            mPresenter = createPresent();//TODO 后期做非空判断,createPresent();可能返回空
        }
        if (!mPresenter.isAttachView()) {
            mPresenter.attachView(this);//presenter与view断开连接
        }
    }

    /**
     * 显示进度对话框
     *
     * @param msg        进度条加载内容
     * @param cancelable
     */
    @Override
    public void showLoadingDialog(String msg, boolean cancelable) {
        if (mProgressDialog == null) {
            mProgressDialog = LoadingDialog.getInstance(this, msg);//实例化progressDialog
            mProgressDialog.setCanceledOnTouchOutside(cancelable);
        }
        if (!mProgressDialog.isShowing()) {//如果进度条没有显示
            mProgressDialog.show();//显示进度条
        }

    }

    /**
     * 取消进度对话框
     */
    @Override
    public void dismissLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onServerNetException() {
        //默认空实现,需要子类具体实现
    }

    /**
     * 1.父布局固定为相对布局
     * 2.需要子类在布局文件中添加如下代码
     * <include
     * android:id="@+id/no_network_tip_panel"
     * layout="@layout/no_network_tip" />
     *
     * @param noNetwork true 无网络,false 有网络
     */
    @Override
    public void onNoNetwork(boolean noNetwork) {
        if (mNoNetworkTipPanel != null) {
            if (noNetwork) {
                mNoNetworkTipPanel.setVisibility(View.VISIBLE);
            } else {
                mNoNetworkTipPanel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFirstEnterNoNetwork() {
        //默认空实现,需要子类具体实现
        if (mNoNetworkView != null) {
            mNoNetworkView.setStatusViewType(Status.STATUS_NO_NETWORK_SHOW);
            mNoNetworkView.setIStatusViewItemCallback(this);
        }
    }

    /**
     * 是否在开启新界面的时候从栈中移除当前界面
     * 应用场景: startActivity() 后立马调用 finish()时
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus && mFinish) {
            Observable.timer(800, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if (!BaseMvpActivity.this.isActivityTop(BaseMvpActivity.this.getClass(), BaseMvpActivity.this)) {
                                mFinish = false;
                                BaseMvpActivity.this.finish();
                            }
                        }
                    });
        }
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 是否在开启新界面的时候从栈中移除当前界面
     * 应用场景: startActivity() 后立马调用 finish()时
     */
    public void finishSelfAfterStartActivity() {
        mFinish = true;
    }

    /**
     * 判断某activity是否处于栈顶
     *
     * @return true在栈顶 false不在栈顶
     */
    private boolean isActivityTop(Class cls, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = null;
        if (manager != null) {
            name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
            return name.equals(cls.getName());
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().unregisterNetStatusObserver(this);
        dismissLoadingDialog();
        mPresenter.detachView();
        mPresenter.clearDisposables();
        mPresenter = null;
        super.onDestroy();
    }

    /**
     * 方法有两个调用时机
     * 1.子类初始化的时候
     * 2.收到系统广播网络变化的时候
     *
     * @param type NetUtil.NET_TYPE_2G 等
     */
    @Override
    public void updateNetStatus(int type) {
        switch (type) {
            case NetUtil.NET_NO_CONNECTION:
                onNoNetwork(true);
                break;
            default:
                onNoNetwork(false);
        }
    }

    /**
     * 子类实现必须调用super.onNoNetworkBtnRetryClick(networkAvailable)
     * 因为父类控制无网络视图的显隐性,子类没有控制权
     *
     * @param networkAvailable 网络是否可用
     */
    @Override
    public void onNoNetworkBtnRetryClick(boolean networkAvailable) {
        if (networkAvailable) {
            mNoNetworkView.setStatusViewType(Status.STATUS_NO_NETWORK_HIDE);
        } else {
            mNoNetworkView.setStatusViewType(Status.STATUS_NO_NETWORK_SHOW);
        }
    }

    /**
     * 适配5.0及以上状态栏沉浸式(里面设置颜色0xfff08519, ff不能删去必须是不透明的)
     */
    @Override
    public int setStatusBarColor() {
        return 0xffbdbdbd;
    }
}
