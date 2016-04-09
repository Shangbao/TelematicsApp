package com.hangon.home.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fd.ourapplication.R;
import com.hangon.common.JsonUtil;
import com.hangon.fragment.CarFragment;
import com.hangon.fragment.MusicFragment;
import com.hangon.fragment.OrderFragment;
import com.hangon.fragment.UserFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/1.
 */
public class HomeActivity extends Activity implements View.OnClickListener{

    //定位四个帧fragment
    private Fragment carFragment=new CarFragment();
    private  Fragment musicFragment=new MusicFragment();
    private  Fragment orderFragment=new OrderFragment();
    private  Fragment userFragment=new UserFragment();

    //tab中的四个帧布局
    private FrameLayout carFrameLayout,musicFrameLayout,orderFrameLayout,userFrameLayout;

    //tab中的四个图片组件
    private TextView carTextView,musicTextView,orderTextView,userTextView;

    //tab中的四个图片对应的文字
    private ImageView carImageView,musicImageView,orderImageView,userImageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initFragment();
        initClickEvent();
    }

    /**
    初始化所有的fragment
     */
    private  void initFragment(){
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        if(!carFragment.isAdded()){
            transaction.add(R.id.content,carFragment);
            transaction.hide(carFragment);
        }
        if(!musicFragment.isAdded()){
            transaction.add(R.id.content,musicFragment);
            transaction.hide(musicFragment);
        }
        if(!orderFragment.isAdded()){
            transaction.add(R.id.content,orderFragment);
            transaction.hide(orderFragment);
        }
        if(!userFragment.isAdded()){
            transaction.add(R.id.content,userFragment);
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
     *
     */
    private void hideAllFragement(FragmentTransaction transaction) {
        transaction.hide(carFragment);
        transaction.hide(musicFragment);
        transaction.hide(orderFragment);
        transaction.hide(userFragment);
    }

    /**
     * 初始化组件
     */
    private  void initView(){
        carFrameLayout= (FrameLayout) findViewById(R.id.carLayout);
        musicFrameLayout= (FrameLayout) findViewById(R.id.musicLayout);
        orderFrameLayout= (FrameLayout) findViewById(R.id.orderLayout);
        userFrameLayout= (FrameLayout) findViewById(R.id.userLayout);

        carImageView= (ImageView) findViewById(R.id.carImageView);
        musicImageView= (ImageView) findViewById(R.id.musicImageView);
        orderImageView= (ImageView) findViewById(R.id.orderImageView);
        userImageView= (ImageView) findViewById(R.id.userImageView);

        carTextView= (TextView) findViewById(R.id.carTextView);
        musicTextView= (TextView) findViewById(R.id.musicTextView);
        orderTextView= (TextView) findViewById(R.id.orderTextView);
        userTextView= (TextView) findViewById(R.id.userTextView);
    }

    /**
     初始化点击事件
     */
    private void initClickEvent(){
        carFrameLayout.setOnClickListener(this);
        musicFrameLayout.setOnClickListener(this);
        orderFrameLayout.setOnClickListener(this);
        userFrameLayout.setOnClickListener(this);
    }

    /**
     *
     * 触发tab中FramLayout点击事件
     */
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.carLayout:
                clickTab(carFragment);
                break;

            case R.id.musicLayout:
                clickTab(musicFragment);
                break;

            case R.id.orderLayout:
                clickTab(orderFragment);
                break;

            case R.id.userLayout:
                clickTab(userFragment);
                break;
        }

    }

    /**
     * 点击Tab按钮
     *
     */
    private void clickTab(Fragment tabFragment) {
        //清除之前选中的状态
        clearSelected();
        FragmentTransaction transaction=getFragmentManager().beginTransaction();

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
        if (!orderFragment.isHidden()) {
            orderImageView.setImageResource(R.drawable.ic_launcher);
            orderTextView.setTextColor(Color.parseColor("#45C01A"));
        }

        if (!userFragment.isHidden()) {
            userImageView.setImageResource(R.drawable.ic_launcher);
            userTextView.setTextColor(Color.parseColor("#45C01A"));
        }
    }

    /**
     * 根据样式改变选中的装填
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

        if (tabFragment instanceof OrderFragment) {
            orderImageView.setImageResource(R.drawable.login_pic);
            orderTextView.setTextColor(Color.parseColor("#999999"));
        }

        if (tabFragment instanceof UserFragment) {
            userImageView.setImageResource(R.drawable.login_pic);
            userTextView.setTextColor(Color.parseColor("#999999"));
        }
    }



    /*
    private void getUserInfo(){
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("bundle");

        String userName=bundle.getString("userName");
        String  nickname=bundle.getString("nickname");
        String sex=bundle.getString("sex");
        int age=bundle.getInt("age");
        String driverNum=bundle.getString("driverNum");

        Log.e("userName", userName);
        Log.e("nickname", nickname);
        Log.e("sex", sex);
        Log.e("age", age+"");
        Log.e("driverNum", driverNum);
    }*/
}
