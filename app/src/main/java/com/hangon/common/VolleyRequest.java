package com.hangon.common;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.hangon.user.activity.RegisterActivity;

import java.util.Map;

/**
 * Created by Chuan on 2016/4/5.
 */
public class VolleyRequest {
    public static MyStringRequest myStringRequest;
    public static Context context;
    public static void RequestGet(Context mContext,String url,String tag,VolleyInterface vif){
        MyApplication.getHttpQueues().cancelAll(tag);
        myStringRequest =  new MyStringRequest(Method.GET,url,vif.loadingListener(),vif.errorListener());
        myStringRequest.setTag(tag);
        MyApplication.getHttpQueues().add(myStringRequest);
        MyApplication.getHttpQueues().start();
    }

    public static void RequestPost(Context mContext,String url,String tag, final Map<String,String> params,VolleyInterface vif){
        MyApplication.getHttpQueues().cancelAll(tag);
        myStringRequest = new MyStringRequest(Method.POST,url,vif.loadingListener(),vif.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        myStringRequest.setTag(tag);
        MyApplication.getHttpQueues().add(myStringRequest);
        MyApplication.getHttpQueues().start();
    }


}
