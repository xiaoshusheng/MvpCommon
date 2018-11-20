package com.outer.mvpcommon.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.outer.mvpcommon.MyApplication;
import com.outer.mvpcommon.R;
import com.outer.mvpcommon.networkmonitoring.NetStatusObserver;
import com.outer.mvpcommon.status_view.IStatusViewItemCallback;
import com.outer.mvpcommon.status_view.LoadingDialog;
import com.outer.mvpcommon.status_view.NoNetworkView;
import com.outer.mvpcommon.status_view.Status;
import com.outer.mvpcommon.utils.NetUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * Created by xiaomili on 2018/1/29.
 * description:
 * mvp中Fragment基类
 */
public abstract class BaseMvpFragment<P extends IBasePresenter> extends RxFragment implements IBaseView, NetStatusObserver, IStatusViewItemCallback {
    private LoadingDialog mProgressDialog;//登录进度条
    protected P mPresenter;
    private NoNetworkView mNoNetworkView;
    private TextView mNoNetworkTipPanel;

    public abstract P createPresent();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().registerNetStatusObserver(this);
        if (mPresenter == null) {
            mPresenter = createPresent();
        }
        if (!mPresenter.isAttachView()) {
            mPresenter.attachView(this);//presenter与view断开连接
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mNoNetworkView = view.findViewById(R.id.no_network_view);
        mNoNetworkTipPanel = view.findViewById(R.id.no_network_tip_panel);
        if (!MyApplication.getInstance().isNetConnection()) {
            onFirstEnterNoNetwork();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter == null) {
            mPresenter = createPresent();
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
            mProgressDialog = LoadingDialog.getInstance(getActivity(), msg);//实例化progressDialog
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
     * 子类对网络变化具有实时的感应功能并显示相应的视图(参考蓝湖中对应的{没有网络}效果图)
     * 需要子类在布局文件中添加如下代码
     * <include
     * android:id="@+id/no_network_tip_panel"
     * layout="@layout/no_network_tip" />
     *
     * @param noNetwork true 无网络,false 有网络
     */
    @Override
    public void onNoNetwork(boolean noNetwork) {
        //默认实现,不需要子类具体实现,需要子类调用前添加 super.onNoNetwork(noNetwork);
        if (mNoNetworkTipPanel != null) {
            if (noNetwork) {
                mNoNetworkTipPanel.setVisibility(View.VISIBLE);
            } else {
                mNoNetworkTipPanel.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 子类首次加载时并且在无网络时显示无网络视图(参考蓝湖中对应的{没有网络}效果图)
     * 只需要在子类中添加 <include layout="@layout/no_network_view" />即可
     */
    @Override
    public void onFirstEnterNoNetwork() {
        //默认实现,不需要子类具体实现
        if (mNoNetworkView != null) {
            mNoNetworkView.setStatusViewType(Status.STATUS_NO_NETWORK_SHOW);
            mNoNetworkView.setIStatusViewItemCallback(this);
        }
    }

//    @Override
//    public void onDestroyView() {
//        mPresenter.detachView();
//        mPresenter = null;
//        super.onDestroyView();
//    }

    @Override
    public void onDestroy() {
        MyApplication.getInstance().unregisterNetStatusObserver(this);
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

    @Override
    public int setStatusBarColor() {
        return 0xffbdbdbd;
    }
}
