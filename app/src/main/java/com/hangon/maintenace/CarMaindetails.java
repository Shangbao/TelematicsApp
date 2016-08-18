package com.hangon.maintenace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
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
import com.hangon.common.MyApplication;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.map.util.JudgeNet;
import com.hangon.saying.activity.PublishedActivity;
import com.hangon.saying.activity.TestPicActivity;
import com.hangon.user.activity.LoginActivity;

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
    private TextView site_number;
    private ImageView car_over_rall;
    private ImageView all_picture;
    //关于poi搜索
    private String uid;
    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch = null;//检索建议
     //topbar
    private ImageView topLeft;
    private ImageView topRight;
    private TextView topTittle;
     private String distance;
  //进入map判断
    JudgeNet judge;
    //进去map传值
    private String address;
    private double   lat;
    private double  lon;
    //评分
    double score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.carmaindetails);
        init();
        receiveData();
        initOnclick();
        loadImg();
    }

    private void receiveData() {
        uid=this.getIntent().getExtras().getString("uid");
        Log.d("loca",uid);
        distance=this.getIntent().getExtras().getString("distance");
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
                if("暂无信息".equals(mCarMianPhone.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(),"暂无本店电话信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                new PopupWindows(CarMaindetails.this, linearLayout);
            }}
        });
    }

    private void init() {

        topLeft=(ImageView)findViewById(R.id.topbar_left);
        topRight=(ImageView)findViewById(R.id.topbar_right);
        topTittle=(TextView)findViewById(R.id.topbar_title);
        topLeft.setBackgroundResource(R.drawable.xcd_001);
        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topRight.setImageResource(R.drawable.xcdxq_001);
            topRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    judge = new JudgeNet();
                    judge.setStates(1);
                    judge.setAppointRoute(1);

                    //   bundle.putString("endaddress", address);
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
        mCarMianAddress = (TextView) findViewById(R.id.car_site_address);
        mCarMianMeters = (TextView) findViewById(R.id.car_site_meter);
        mCarMianName = (TextView) findViewById(R.id.car_site_name);
        mCarMianPhone = (TextView) findViewById(R.id.site_phone);
        mCarMianWorktime = (TextView) findViewById(R.id.car_site_date );
        site_number=(TextView)findViewById(R.id.car_number_fen);
        car_over_rall=(ImageView)findViewById(R.id.car_overall_rating);
        //全景静态图
        all_picture=(ImageView)findViewById(R.id.all_picture);
        //Poi监听模块初始化
        mPoiSearch = PoiSearch.newInstance().newInstance();
        mSuggestionSearch = SuggestionSearch.newInstance();
        //监听回调结果
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

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
            if(poiDetailResult.getTelephone()!=null&&!poiDetailResult.getTelephone().equals("")){
            mCarMianPhone.setText(poiDetailResult.getTelephone());
            }
            else {
                mCarMianPhone.setText("暂无信息");
            }
            mCarMianMeters.setText(distance+"米");
            mCarMianAddress.setText(poiDetailResult.getAddress());
            site_number.setText(poiDetailResult.getOverallRating()+"分");
            score=poiDetailResult.getOverallRating();

            if((int)score==0){
                car_over_rall.setBackgroundResource(R.drawable.xcd_002);
            }
            if((int)score==1) {
                car_over_rall.setBackgroundResource(R.drawable.xcd_002);
            }
            if ((int)score==2){
                car_over_rall.setBackgroundResource(R.drawable.xcd_003);
            }
            if((int)score==3){
                car_over_rall.setBackgroundResource(R.drawable.xcd_004);
            }
            if((int)score==4){
                car_over_rall.setBackgroundResource(R.drawable.xcd_005);
            }
            if((int)score==5){
                car_over_rall.setBackgroundResource(R.drawable.xcd_006);
            }
            Log.d("asas",lat+":asas"+lon);
        }
    }
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }
    public void loadImg(){

        String url="http://api.map.baidu.com/panorama/v2?ak=8ZcbE4SeBsjWyfulkiqswRaHOm1mFZV8&mcode=84:62:7A:13:86:10:06:F6:77:86:66:B5:46:E6:58:B1:A7:F4:85:BB;baidumapsdk.demo&width=512&height=512&poiid="+uid+"&fov=180&qq-pf-to=pcqq.c2c";
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                all_picture.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(CarMaindetails.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getHttpQueues().add(request);
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

