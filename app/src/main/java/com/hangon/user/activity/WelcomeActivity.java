package com.hangon.user.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/31.
 */
public class WelcomeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ImageView welcomeLogo =(ImageView)findViewById(R.id.welcomeLogo);
        Animation welcomeAnim= AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.welcome_anim);
        welcomeLogo.startAnimation(welcomeAnim);

        Timer timer=new Timer();
        timer.schedule(new wait(),4000);
    }

    class wait extends TimerTask{
        public void run(){
            Intent toLogin=new Intent();
            toLogin.setClass(WelcomeActivity.this, LoginActivity.class);
            if(Constants.VERSION>5){
            overridePendingTransition(R.anim.welcome_anim,R.anim.welcome_out_anim);
            }
            startActivity(toLogin);
            WelcomeActivity.this.finish();
        }
    }


}
