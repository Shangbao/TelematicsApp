package com.hangon.home.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fd.ourapplication.R;
import com.hangon.fragment.car.CarFragment;
import com.hangon.fragment.music.MusicFragment;
import com.hangon.fragment.order.ZnwhFragment;
import com.hangon.fragment.order.ZnwhService;
import com.hangon.fragment.userinfo.UserFragment;
import com.hangon.order.activity.PersonalInformationData;
import com.hangon.weather.Weather;
import com.hangon.weather.WeatherService;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/4/1.
 */
public class HomeActivity extends Activity implements View.OnClickListener {

    //fragment的位置position
    int position;
    //定位四个帧fragment
    private Fragment carFragment = new CarFragment();
    private Fragment musicFragment = new MusicFragment();
    private Fragment znwhFragment = new ZnwhFragment();
    private Fragment userFragment = new UserFragment();
    private Intent weatherIntent;

    //tab中的四个帧布局
    private FrameLayout carFrameLayout, musicFrameLayout, znwhFrameLayout, userFrameLayout;


    //tab中的四个图片组件
    private TextView carTextView, musicTextView, znwhTextView, userTextView;

    //tab中的四个图片对应的文字
    private ImageView carImageView, musicImageView, znwhImageView, userImageView;

//    private ServiceConnection conn = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            binder = (WeatherService.MyBinder) service;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Toast.makeText(HomeActivity.this, "亲，恐怕无法更新天气！", Toast.LENGTH_SHORT).show();
//        }
//    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        weatherIntent = new Intent(this, WeatherService.class);
        startService(weatherIntent);

        initView();
        initFragment();
        initClickEvent();
        int position = getIntent().getIntExtra("id", 0);
        if (position != 0) {
            getTab(position);
        } else {
            getTab(1);
        }
    }

    /**
     * 初始化所有的fragment
     */
    private void initFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (!carFragment.isAdded()) {
            transaction.add(R.id.content, carFragment);
            transaction.hide(carFragment);
        }
        if (!musicFragment.isAdded()) {
            transaction.add(R.id.content, musicFragment);
            transaction.hide(musicFragment);
        }
        if (!znwhFragment.isAdded()) {
            transaction.add(R.id.content, znwhFragment);
            transaction.hide(znwhFragment);
//            Intent intent = getIntent();
//            Bundle bundle = intent.getBundleExtra("ZnwhInfo");
//            znwhFragment.setArguments(bundle);
        }
        if (!userFragment.isAdded()) {
            transaction.add(R.id.content, userFragment);
            transaction.hide(userFragment);
        }
        //隐藏所有的Fragment
        hideAllFragement(transaction);

        //显示第一个Fragment
        transaction.show(carFragment);
        transaction.commit();
    }

    /**
     * 隐藏所有的fragment
     */
    private void hideAllFragement(FragmentTransaction transaction) {
        transaction.hide(carFragment);
        transaction.hide(musicFragment);
        transaction.hide(znwhFragment);
        transaction.hide(userFragment);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        carFrameLayout = (FrameLayout) findViewById(R.id.carLayout);
        musicFrameLayout = (FrameLayout) findViewById(R.id.musicLayout);
        znwhFrameLayout = (FrameLayout) findViewById(R.id.znwhLayout);
        userFrameLayout = (FrameLayout) findViewById(R.id.userLayout);

        carImageView = (ImageView) findViewById(R.id.carImageView);
        musicImageView = (ImageView) findViewById(R.id.musicImageView);
        znwhImageView = (ImageView) findViewById(R.id.znwhImageView);
        userImageView = (ImageView) findViewById(R.id.userImageView);

        carTextView = (TextView) findViewById(R.id.carTextView);
        musicTextView = (TextView) findViewById(R.id.musicTextView);
        znwhTextView = (TextView) findViewById(R.id.znwhTextView);
        userTextView = (TextView) findViewById(R.id.userTextView);
    }

    /**
     * 初始化点击事件
     */
    private void initClickEvent() {
        carFrameLayout.setOnClickListener(this);
        musicFrameLayout.setOnClickListener(this);
        znwhFrameLayout.setOnClickListener(this);
        userFrameLayout.setOnClickListener(this);
    }

    /**
     * 触发tab中FramLayout点击事件
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carLayout:
                position = 1;
                getTab(position);
                break;

            case R.id.musicLayout:
                position = 2;
                getTab(position);
                break;

            case R.id.znwhLayout:
                position = 3;
                getTab(position);
                break;

            case R.id.userLayout:
                position = 4;
                getTab(position);
                break;
        }

    }

    /**
     * 点击Tab按钮
     */
    private void clickTab(Fragment tabFragment) {
        //清除之前选中的状态
        clearSelected();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        //隐藏所有的Fragment
        hideAllFragement(transaction);

        //显示点中的fragment
        transaction.show(tabFragment);

        //提交事务
        transaction.commit();

        //改变选中tab的样式
        changeTabStyle(tabFragment);

    }

    /**
     * 通过获取id值来确定底部切换栏
     */
    private void getTab(int position) {
        switch (position) {
            case 1:
                clickTab(carFragment);
                break;

            case 2:
                clickTab(musicFragment);
                break;

            case 3:
                clickTab(znwhFragment);
                break;

            case 4:
                clickTab(userFragment);
                break;
        }
    }


    /**
     * 清除之前所有的样式
     */
    private void clearSelected() {
        if (!carFragment.isHidden()) {
            carImageView.setImageResource(R.drawable.ic_launcher);
            carTextView.setTextColor(Color.parseColor("#45C01A"));
        }
        if (!musicFragment.isHidden()) {
            musicImageView.setImageResource(R.drawable.ic_launcher);
            musicTextView.setTextColor(Color.parseColor("#45C01A"));
        }
        if (!znwhFragment.isHidden()) {
            znwhImageView.setImageResource(R.drawable.ic_launcher);
            znwhTextView.setTextColor(Color.parseColor("#45C01A"));
        }

        if (!userFragment.isHidden()) {
            userImageView.setImageResource(R.drawable.ic_launcher);
            userTextView.setTextColor(Color.parseColor("#45C01A"));
        }
    }

    /**
     * 根据样式改变选中的装填
     *
     * @param tabFragment
     */
    private void changeTabStyle(Fragment tabFragment) {
        if (tabFragment instanceof CarFragment) {
            carImageView.setImageResource(R.drawable.login_pic);
            carTextView.setTextColor(Color.parseColor("#999999"));
        }

        if (tabFragment instanceof MusicFragment) {
            musicImageView.setImageResource(R.drawable.login_pic);
            musicTextView.setTextColor(Color.parseColor("#999999"));
        }

        if (tabFragment instanceof ZnwhFragment) {
            znwhImageView.setImageResource(R.drawable.login_pic);
            znwhTextView.setTextColor(Color.parseColor("#999999"));
        }

        if (tabFragment instanceof UserFragment) {
            userImageView.setImageResource(R.drawable.login_pic);
            userTextView.setTextColor(Color.parseColor("#999999"));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("id");
        getTab(position);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //记录当前的position
        outState.putInt("id", position);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(weatherIntent);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer timer = null;
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
