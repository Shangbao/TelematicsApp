package com.hangon.user.service;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hangon.home.activity.HomeActivity;
import com.hangon.user.activity.LoginActivity;

/**
 * Created by Administrator on 2016/3/31.
 */

public class LoginListener implements View.OnClickListener {
    View view;
    @Override
    public void onClick(View v) {

        login();
        Intent toHome=new Intent();
        Context loginContext=v.getContext();
        toHome.setClass(loginContext,HomeActivity.class);
        loginContext.startActivity(toHome);

    }

    private void login(){


    }
}
