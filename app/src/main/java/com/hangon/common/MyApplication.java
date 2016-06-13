package com.hangon.common;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/3/31.
 */
public class MyApplication extends Application {
    public static RequestQueue queues;
    public static int memoryCacheSize;

    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        memoryCacheSize = getMemoryCacheSize();
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }

    /**
     *
     * @return 得到需要分配的缓存大小，这里用八分之一的大小来做
     */
    public int getMemoryCacheSize() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        return maxMemory / 8;
    }

}
