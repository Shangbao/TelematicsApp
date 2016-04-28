package com.hangon.carInfoManage.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
public class YichangActivity extends Activity implements View.OnClickListener{

    EditText mileage;//里程数
    EditText oddGasAmount;//剩余油量
    EditText isGoodEngine;//引擎状况
    EditText isGoodTran;//变速器状况
    EditText isGoodLight;//车灯状况

    Button btnYcjb;//异常警报
    Button btnScsjs;//生成随机数

    YcglVO ycglVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_yichang);
        init();
    }

    //初始化组件
    private void init(){
        //初始化实例
        mileage= (EditText) findViewById(R.id.yichang_mileage);
        oddGasAmount= (EditText) findViewById(R.id.yichang_oddGasAmount);
        isGoodEngine= (EditText) findViewById(R.id.yichang_isGoodEngine);
        isGoodTran= (EditText) findViewById(R.id.yichang_isGoodTran);
        isGoodLight= (EditText) findViewById(R.id.yichang_isGoodLight);

        btnScsjs= (Button) findViewById(R.id.btnScsjs);
        btnYcjb= (Button) findViewById(R.id.btnYcjb);
        btnYcjb.setOnClickListener(this);
        btnScsjs.setOnClickListener(this);

    }

    private void setEditextValue(){
        mileage.setText(ycglVO.getMileage()+"");
        oddGasAmount.setText(ycglVO.getOddGasAmount()+"");
        isGoodEngine.setText(ycglVO.getYjNum()+"");
        isGoodTran.setText(ycglVO.getBsNum()+"");
        isGoodLight.setText(ycglVO.getCdNum()+"");
    }

    public void onClick(View v) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        switch (v.getId()){
            case R.id.btnYcjb:

                break;
            case R.id.btnScsjs:
                generateRound();
                setEditextValue();
                break;
        }
    }

    private void generateRound(){
        int mileage=(new Random()).nextInt(150000);
        int oddGasAmount=(int)(100*Math.random());
        double yjNum=Math.random();
        double bsNum=Math.random();
        double cdNum=Math.random();
        ycglVO=new YcglVO();

        Log.e("aa", mileage + "");
        ycglVO.setMileage(mileage);
        ycglVO.setOddGasAmount(oddGasAmount);
        ycglVO.setBsNum(bsNum);
        ycglVO.setYjNum(yjNum);
        ycglVO.setCdNum(cdNum);
    }
}
