package com.outer.mvpcommon.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.outer.mvpcommon.constant.Constant;
import com.outer.mvpcommon.utils.LogUtils;
import com.outer.mvpcommon.utils.NetworkUtils;
import com.outer.mvpcommon.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiaomili on 2018/1/17.
 * description:
 * 封装retrofit+RxJava
 */

public class NetApi {
    private static final String TAG = NetApi.class.getSimpleName();
    private Retrofit retrofit;
    private IService service;

    /**
     * 网络请求超时时间毫秒
     */
    int DEFAULT_TIMEOUT = 20000;

    private NetApi() {
        /**
         * log 拦截器设置
         */
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.d("OKHttp-----", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        /**
         * 缓存设置
         */
        File cacheFile = new File(Utils.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .build();
        /**
         * 设置gson 时间解析格式可以避免不同的Local影响时间显示
         */
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.domain)
                .build();
        service = retrofit.create(IService.class);
    }

    /**
     * 创建单例
     */
    private static class SingletonHolder {
        private static final NetApi INSTANCE = new NetApi();
    }

    /**
     * 获取
     * @return
     */
    public static IService getService() {
        return SingletonHolder.INSTANCE.service;
    }

    /**
     * 缓存拦截器设置
     */
    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected()) {  //没网强制从缓存读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d("Okhttp", "no network");
            }
            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}
