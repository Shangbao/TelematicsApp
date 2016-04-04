package com.hangon.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fd.ourapplication.R;
import com.hangon.common.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public class HomeActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");

        String userName=bundle.getString("userName");
        String  nickname=bundle.getString("nickname");
        String sex=bundle.getString("sex");
        int age=bundle.getInt("age");
        String driverNum=bundle.getString("driverNum");


        Map map=new HashMap();
        map.put("userName",userName);
        map.put("nickname",nickname);
        map.put("sex",sex);
        map.put("age", age);
        map.put("driverNum",driverNum);

        List list= new  ArrayList();
        list.add(userName);
        list.add(nickname);
        list.add(sex);
        list.add(age);
        list.add(driverNum);

        Log.e("userName", userName);
        Log.e("nickname", nickname);
        Log.e("sex", sex);
        Log.e("age", age+"");
        Log.e("driverNum", driverNum);

    }
}
