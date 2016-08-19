package com.hangon.home.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.JsonUtil;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.fragment.car.CarFragment;
import com.hangon.fragment.music.MusicFragment;
import com.hangon.fragment.order.ZnwhFragment;
import com.hangon.fragment.userinfo.UserFragment;
import com.hangon.push.GetLocationService;
import com.hangon.push.QzLocationService;
import com.hangon.weather.WeatherService;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/4/1.
 */
public class HomeActivity extends Activity implements View.OnClickListener,MusicFragment.BackClickListener {

    //fragment的位置position
    int position;
    //定位四个帧fragment
    private Fragment carFragment = new CarFragment();
    private Fragment musicFragment = new MusicFragment();
    private Fragment znwhFragment = new ZnwhFragment();
    private Fragment userFragment = new UserFragment();
    private Intent weatherIntent;
    private Intent pushIntent;
    private RelativeLayout bottomArea;
    private ImageView tabTop;

    public static int FragmentIndex;

    //tab中的四个帧布局
    private FrameLayout carFrameLayout, musicFrameLayout, znwhFrameLayout, userFrameLayout;


    //tab中的四个图片组件
    private TextView carTextView, musicTextView, znwhTextView, userTextView;

    //tab中的四个图片对应的文字
    private ImageView carImageView, musicImageView, znwhImageView, userImageView;

    WeatherService.WeatherBinder binder;
    FragmentTransaction transaction;

    public final static int INTENT_SETCARINFO = 1;
    public final static int INTENT_WZCX = 2;
    public final static int INTENT_USERICON = 3;
    public final static int INTENT_UPDATEUSER = 4;
    public final static int INTENT_SAYING= 5;

    //public final static String MUSIC_BACK_ACTION = "com.hangon.home.activity.HomeActivity";   音乐back键

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (WeatherService.WeatherBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(HomeActivity.this, "天气部分出现错误!", Toast.LENGTH_SHORT).show();
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ShareSDK.initSDK(HomeActivity.this);
        weatherIntent = new Intent(this, WeatherService.class);
        pushIntent = new Intent(this, GetLocationService.class);
        bindService(weatherIntent, conn, Service.BIND_AUTO_CREATE);
        startService(pushIntent);
        if(JPushInterface.isPushStopped(HomeActivity.this)){
            JPushInterface.resumePush(HomeActivity.this);
        }
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        initView();
        initFragment();
        initClickEvent();
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
        }
        if (!userFragment.isAdded()) {
            transaction.add(R.id.content, userFragment);
            transaction.hide(userFragment);
        }
        //隐藏所有的Fragment
        hideAllFragement(transaction);

        //显示第一个Fragment
        transaction.show(carFragment);
        position=1;
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
        bottomArea = (RelativeLayout) findViewById(R.id.bottomArea);
        tabTop = (ImageView) findViewById(R.id.tabTop);
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
                HomeActivity.FragmentIndex = position;
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
                bottomArea.setVisibility(View.VISIBLE);
                break;

            case 2:
                clickTab(musicFragment);
                bottomArea.setVisibility(View.GONE);
                break;

            case 3:
                clickTab(znwhFragment);
                bottomArea.setBackgroundColor(Color.WHITE);
                bottomArea.setVisibility(View.VISIBLE);
                tabTop.setVisibility(View.VISIBLE);
                break;

            case 4:
                clickTab(userFragment);
                bottomArea.setBackgroundColor(Color.WHITE);
                bottomArea.setVisibility(View.VISIBLE);
                tabTop.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 清除之前所有的样式
     */
    private void clearSelected() {

            carImageView.setImageResource(R.drawable.grzx_45);
            carTextView.setTextColor(Color.parseColor("#555555"));


            musicImageView.setImageResource(R.drawable.grzx_48);
            musicTextView.setTextColor(Color.parseColor("#555555"));


            znwhImageView.setImageResource(R.drawable.grzx_50);
            znwhTextView.setTextColor(Color.parseColor("#555555"));


        userImageView.setImageResource(R.drawable.grzx_15);
        userTextView.setTextColor(Color.parseColor("#555555"));

            userImageView.setImageResource(R.drawable.grzx_15);
            userTextView.setTextColor(Color.parseColor("#555555"));

    }

    /**
     * 清除之前所有的样式
     */
    private void clearSelected1() {
        bottomArea.setBackground(null);
        tabTop.setVisibility(View.GONE);
        carImageView.setImageResource(R.drawable.czs_23);
        carTextView.setTextColor(Color.parseColor("#ffffff"));
        musicImageView.setImageResource(R.drawable.czs_29);
        musicTextView.setTextColor(Color.parseColor("#ffffff"));
        znwhImageView.setImageResource(R.drawable.czs_26);
        znwhTextView.setTextColor(Color.parseColor("#ffffff"));
        userImageView.setImageResource(R.drawable.czs_32);
        userTextView.setTextColor(Color.parseColor("#ffffff"));

    }

    /**
     * 根据样式改变选中的装填
     *
     * @param tabFragment
     */
    private void changeTabStyle(Fragment tabFragment) {
        if (tabFragment instanceof CarFragment) {
            clearSelected1();
        }

        if (tabFragment instanceof MusicFragment) {
            musicImageView.setImageResource(R.drawable.login_pic);
            musicTextView.setTextColor(Color.parseColor("#019b79"));
        }

        if (tabFragment instanceof ZnwhFragment) {
            znwhImageView.setImageResource(R.drawable.wh_07);
            znwhTextView.setTextColor(Color.parseColor("#019b79"));

        }

        if (tabFragment instanceof UserFragment) {
            userImageView.setImageResource(R.drawable.grzx_52);
            userTextView.setTextColor(Color.parseColor("#019b79"));
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getTab(3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserUtil.instance(HomeActivity.this);
        changeLoginFlag(0,UserUtil.getInstance().getIntegerConfig("userId") );//清除登陆状态
        ShareSDK.stopSDK(this);
        binder.stopWeather();
        unbindService(conn);
        stopService(pushIntent);
        JPushInterface.stopPush(HomeActivity.this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_SETCARINFO:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("id", 0);
                    if (position != 0) {
                        getTab(position);
                    } else {
                        getTab(1);
                    }
                }
                break;
            case INTENT_WZCX:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("id", 0);
                    if (position != 0) {
                        getTab(position);
                    } else {
                        getTab(1);
                    }
                }
                break;
            case INTENT_SAYING:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("id", 0);
                    if (position != 0) {
                        getTab(position);
                    } else {
                        getTab(1);
                    }
                }
                break;

        }
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

    @Override
    //音乐返回键回调接口
    public void backClick(int position) {
        if(position!=0){
            this.position=position;
            getTab(position);
        }
    }
        /**
     * 把登陆状态变为登陆状态
     */
    private void changeLoginFlag(int loginFlag,int userId){
        String url=Constants.CHANGE_LOGINFLAG_URL;
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("loginFlag",loginFlag+"");
        map.put("userId",userId+"");
        VolleyRequest.RequestPost(HomeActivity.this, url, "changeLoginFlag", map, new VolleyInterface(HomeActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Map map=new HashMap();
                map=JsonUtil.jsonToMap(result);
                boolean success= (boolean) map.get("success");
                String msg= (String) map.get("msg");
                if(success){
                    Toast.makeText(HomeActivity.this,msg,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(HomeActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }

}
