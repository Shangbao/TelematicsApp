package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fd.ourapplication.R;
import com.hangon.bean.carInfo.YcglVO;

import java.util.Random;

/**
 * Created by Administrator on 2016/4/28.
 */
public class WeizhangActivity extends Activity {



    YcglVO ycglVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_weizhang);

    }



}
