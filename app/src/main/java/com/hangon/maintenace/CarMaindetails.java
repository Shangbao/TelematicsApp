package com.hangon.maintenace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.fd.ourapplication.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.map.util.JudgeNet;
import com.hangon.saying.activity.PublishedActivity;
import com.hangon.saying.activity.TestPicActivity;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by mykonons on 2016/8/3.
 */
public class CarMaindetails extends Activity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    private TextView mCarMianName;
    private TextView mCarMianMeters;
    private TextView mCarMianAddress;
    private TextView mCarMianPhone;
    private TextView mCarMianWorktime;
    private LinearLayout linearLayout;
    //关于poi搜索
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch = null;//检索建议
     //topbar
    private ImageButton topLeft;
    private ImageButton topRight;
    private TextView topTittle;

  //进入map判断
    JudgeNet judge;
    //进去map传值
    private String address;
    private double   lat;
    private double  lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.carmaindetails);
        init();
        receiveData();
        initOnclick();



    }

    private void receiveData() {
        final String uid=this.getIntent().getExtras().getString("uid");
        Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendPoi(uid);
            }
        }).start();
    }

    private void initOnclick() {
        mCarMianPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopupWindows(CarMaindetails.this, linearLayout);
            }
        });
    }

    private void init() {

        topLeft=(ImageButton)findViewById(R.id.topbar_left);
        topRight=(ImageButton)findViewById(R.id.topbar_right);
        topTittle=(TextView)findViewById(R.id.topbar_title);

        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
            topRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    judge=new JudgeNet();
                    judge.setStates(1);
                    judge.setAppointRoute(1);

                 //   bundle.putString("endaddress", address);
                   ;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bundle bundle = new Bundle();
                            bundle.putDouble("lon", lon);
                            bundle.putDouble("lat", lat);
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), MapMainActivity.class);
                            intent.putExtra("endaddress", bundle);
                            startActivity(intent);
                        }
                    }).start();

                }
            });

        linearLayout=(LinearLayout)findViewById(R.id.linearLayout1);
        mCarMianAddress = (TextView) findViewById(R.id.carmaindetails_address);
        mCarMianMeters = (TextView) findViewById(R.id.carmaindetails_meters);
        mCarMianName = (TextView) findViewById(R.id.carmaindetails_name);
        mCarMianPhone = (TextView) findViewById(R.id.carmaindetails_phone_number);
        mCarMianWorktime = (TextView) findViewById(R.id.carmaindetails_worktime);
        //Poi监听模块初始化
        mPoiSearch = PoiSearch.newInstance().newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        //监听回调结果
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
       ;
    }
   class  wait extends TimerTask{

       @Override
       public void run() {

       }
   }
    //发起搜索
    private void sendPoi(String uid) {
        mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        Toast.makeText(getApplicationContext(), poiDetailResult.getShopHours(), Toast.LENGTH_SHORT).show();
        if (poiDetailResult == null
                || poiDetailResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(getApplicationContext(), "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }
        if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {
            mCarMianName.setText(poiDetailResult.getName());
            lat=poiDetailResult.getLocation().latitude;
            lon=poiDetailResult.getLocation().longitude;
            address=poiDetailResult.getAddress();
            topTittle.setText(poiDetailResult.getName());
            if (poiDetailResult.getShopHours().toString().trim().length() == 0) {
                mCarMianWorktime.setText("暂无");
            } else {
                mCarMianWorktime.setText(poiDetailResult.getShopHours());
            }

            mCarMianPhone.setText(poiDetailResult.getTelephone());
            mCarMianMeters.setText(DistanceUtil.getDistance(poiDetailResult.getLocation(), poiDetailResult.getLocation()) + "");
            mCarMianAddress.setText(poiDetailResult.getAddress());
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }

    public class PopupWindows extends PopupWindow {
        public PopupWindows(Context mContext, View parent) {
            View view = View
                    .inflate(mContext, R.layout.carmain_detailse_pop, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.car_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_hujio);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);

            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mCarMianPhone.getText().toString()));
                    startActivity(intent);
                    dismiss();
                    finish();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }
}

