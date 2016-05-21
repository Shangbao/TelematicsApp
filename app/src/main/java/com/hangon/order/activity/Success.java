package com.hangon.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.fd.ourapplication.R;
import com.hangon.map.activity.GasSiteDetailsActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fd on 2016/5/5.
 */
public class Success extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        Timer timer=new Timer();
        timer.schedule(new mIntent(),2000);
    }
    public  class mIntent extends TimerTask{
        @Override
        public void run() {
             finish();
//            Intent intent=new Intent();
//            intent.setClass(Success.this, GasSiteDetailsActivity.class);
//            startActivity(intent);

        }
    }
}
