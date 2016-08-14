package com.hangon.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Chuan on 2016/8/10.
 */
public class PushActivity extends Activity {

    Intent intent;

    private String title;
    private String alert;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        initPushMessage();
        tv  = (TextView) findViewById(R.id.pushMessage);
        tv.setText(alert);
    }

    private void initPushMessage(){
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        alert = bundle.getString(JPushInterface.EXTRA_ALERT);
    }
}
