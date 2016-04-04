package com.hangon.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.MyApplication;
import com.hangon.common.MyStringRequest;

/**
 * Created by Administrator on 2016/4/3.
 */
public class TestLogin extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        volleyGet();
    }

    private void volleyGet() {
        String url="http://10.163.0.194:8080/wind/UserLogin?userName=13166837709&userPass=123456";
        MyStringRequest request=new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("xxx",s);
                Toast.makeText(TestLogin.this,s,Toast.LENGTH_SHORT);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(TestLogin.this,volleyError.toString(),Toast.LENGTH_SHORT);
            }
        });
        MyApplication.getHttpQueues().add(request);
    }
}
