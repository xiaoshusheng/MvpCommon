package com.outer.mvpcommon.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 作者：create by YangZ on 2017/11/22 18:16
 * 邮箱：YangZL8023@163.com
 */

public class StreamUtil {
    //可变参数(为了接受多个流用于关闭)
    public static void  close(Closeable... closeables){
        if (closeables != null){
            for (Closeable closeable: closeables) {
                if (closeable!=null){
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
