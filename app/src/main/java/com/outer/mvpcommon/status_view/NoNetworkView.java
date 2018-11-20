package com.outer.mvpcommon.status_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outer.mvpcommon.MyApplication;
import com.outer.mvpcommon.R;
import com.outer.mvpcommon.view.AxisRotatingAnim;


/**
 * Created by XML on 2018/3/21.
 * Description
 * 自定义无网络视图
 */
public class NoNetworkView extends LinearLayout {
    private static final String TAG = NoNetworkView.class.getSimpleName();
    private Context mContext;
    private IStatusViewItemCallback mCallback;

    public NoNetworkView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public NoNetworkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * @param callback
     */
    public void setIStatusViewItemCallback(IStatusViewItemCallback callback) {
        if (callback == null) {
            throw new NullPointerException("IStatusViewItemCallback实例不能为空!");
        }
        mCallback = callback;
    }

    private void init() {
        View mNoNetworkRoot = View.inflate(mContext, R.layout.status_no_nerwork_view, this);
        final ImageView mIvNoNetwork = mNoNetworkRoot.findViewById(R.id.iv_status_no_network);
        TextView mTvNoNetwork01 = mNoNetworkRoot.findViewById(R.id.tv_status_no_network_01);
        TextView mTvNoNetwork02 = mNoNetworkRoot.findViewById(R.id.tv_status_no_network_02);
        Button mBtnNoNetwork = mNoNetworkRoot.findViewById(R.id.btn_status_no_network_retry);
        final AxisRotatingAnim mAxisRotatingAnim = new AxisRotatingAnim();
        mAxisRotatingAnim.setRepeatCount(1);
        mBtnNoNetwork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvNoNetwork.startAnimation(mAxisRotatingAnim);
                if (MyApplication.getInstance().isNetConnection()) {
                    mCallback.onNoNetworkBtnRetryClick(true);
                } else {
                    mCallback.onNoNetworkBtnRetryClick(false);
                }
            }
        });

    }

    /**
     * 控制视图的显隐性
     *
     * @param status Status类中的常量
     */
    public void setStatusViewType(int status) {
        switch (status) {
            case Status.STATUS_NO_NETWORK_HIDE:
                setVisibility(GONE);
                break;
            case Status.STATUS_NO_NETWORK_SHOW:
                setVisibility(VISIBLE);
                break;
        }
    }

}
