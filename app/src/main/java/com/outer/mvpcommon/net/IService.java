package com.outer.mvpcommon.net;


import com.outer.mvpcommon.bean.base.ResponseData;
import com.outer.mvpcommon.constant.Constant;
import com.outer.mvpcommon.mvp.bean.AgrExpertBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 网络请求Retrofit接口
 */
public interface IService {

    /**
     * 课程培训
     */
    @GET(Constant.expert)
    Observable<ResponseData<AgrExpertBean>> getExpertStudy();


}
