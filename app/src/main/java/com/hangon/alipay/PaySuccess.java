package com.hangon.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.fd.ourapplication.R;
import com.hangon.order.activity.MainOrderActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/28.
 */
public class PaySuccess extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        Timer timer=new Timer();
        timer.schedule(new wait(),2000);
    }

    class wait extends TimerTask{

        @Override
        public void run() {
            Intent intent=new Intent();
            intent.setClass(PaySuccess.this, MainOrderActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
