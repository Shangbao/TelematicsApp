package com.hangon.saying.viewPager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.VolleyError;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.order.util.FragmentViewPagerAdapter;
import com.hangon.saying.activity.PublishedActivity;
import com.hangon.saying.util.Location;
import com.hangon.saying.util.MenuHelper;
import com.hangon.saying.util.OnMenuClick;

/**
 * @author weizhi
 * @version 1.0
 */
public class MainActivity extends FragmentActivity implements OnMenuClick {
  //topbar
    private ImageView topLeft;
    private ImageView topRight;
    private TextView topTittle;
    // 下拉菜单shuju
    private MenuHelper mMenuHelper;
    //整个布局
    private LinearLayout container;
    // 下拉菜单shuju
    private List<String> menuData;
    //对应的两个Fragment
    private TextView searchLife;
    //寻求帮助
    private TextView seekHelp;

    //辅助获得地址
    TextView strText;

    //车周围
    private TextView czwTextView;
    //求助
    private TextView qzTextView;
    //实现Tab滑动效果
    private ViewPager mViewPager;
    //动画图片
   // private ImageView cursor;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;
    //动画图片宽度
    private int bmpW;
    //当前页卡编号
    private int currIndex = 0;
    //存放Fragment
    private ArrayList<Fragment> fragmentArrayList;
    //管理Fragment
    private FragmentManager fragmentManager;

    public Context context;

    //切换器的适配器
    FragmentViewPagerAdapter adapter;
    private LocationClient mLocClient;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    AlertDialog.Builder builder;
    Dialog dialog;
    private static final String TAG = "TestSensorActivity";
    private static final int SENSOR_SHAKE = 10;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carlife_main);
        //初始化TextView
        InitTextView();
        mLocClient = ((Location) getApplication()).mLocationClient;
        ((Location) getApplication()).mTv = strText;
        setLocationOption();
        mLocClient.start();

        context = this;
        //初始化ImageView
        InitImageView();

        //初始化Fragment
        InitFragment();

        //初始化ViewPager
        InitViewPager();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
        super.onResume();
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
        dialog = new Dialog(MainActivity.this);
        container = (LinearLayout) findViewById(R.id.zhoubian);
        menuData = new ArrayList<String>();
        menuData.add("写心情");
        menuData.add("周边求助");
        //topbar
        topLeft=(ImageView)findViewById(R.id.topbar_left);
        topRight=(ImageView)findViewById(R.id.topbar_right);
        topTittle=(TextView)findViewById(R.id.topbar_title);
        topTittle.setText("车生活");
        topRight.setImageResource(R.drawable.lj_xx_001);
        mMenuHelper = new MenuHelper(this, topRight, this, menuData, container);
        topLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("id", 3);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        topRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuHelper.showMenu();
            }
        });
        //车周围头标
        czwTextView = (TextView) findViewById(R.id.czw_text);
        //求助头标
        qzTextView = (TextView) findViewById(R.id.qz_text);

        //车周围头标
        //求助头标
        //添加点击事件
        czwTextView.setOnClickListener(new MyOnClickListener(0));
        qzTextView.setOnClickListener(new MyOnClickListener(1));
        strText = (TextView) findViewById(R.id.strText);

    }

    /**
     * 初始化页卡内容区
     */
    private void InitViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.vPager);
        adapter = new FragmentViewPagerAdapter(fragmentManager, fragmentArrayList);
        mViewPager.setAdapter(adapter);

        //让ViewPager缓存2个页面
        mViewPager.setOffscreenPageLimit(2);

        //设置默认打开第一页
        mViewPager.setCurrentItem(0);

        //将顶部文字恢复默认值
        resetTextViewTextColor();
       czwTextView.setBackgroundColor(getResources().getColor(R.color.indictor_selected));
        czwTextView.setTextColor(getResources().getColor((R.color.indictor_selected_text)));
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
      //  cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 获取分辨率宽度
        int screenW = dm.widthPixels;

        bmpW = (screenW / 2);

        //设置动画图片宽度
       // setBmpW(cursor, bmpW);
        offset = 0;

        //动画图片偏移量赋值
        position_one = (int) (screenW / 2.0);
        position_two = position_one * 2;

    }

    /**
     * 初始化Fragment，并添加到ArrayList中
     */
    private void InitFragment() {
        fragmentArrayList = new ArrayList<Fragment>();
        fragmentArrayList.add(new SearchFrage());
        fragmentArrayList.add(new HelpFrage());
        fragmentManager = getSupportFragmentManager();

    }

    /**
     * 头标点击监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnClickListener implements OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 页卡切换监听
     *
     * @author weizhi
     * @version 1.0
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            adapter.update(position);
            switch (position) {
                //当前为页卡1
                case 0:
                    //从页卡1跳转转到页卡2
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        resetTextViewTextColor();
                        czwTextView.setBackgroundColor(getResources().getColor(R.color.indictor_selected));
                        czwTextView.setTextColor(getResources().getColor((R.color.indictor_selected_text)));
                    }

                    //当前为页卡2
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        resetTextViewTextColor();
                        qzTextView.setBackgroundColor(getResources().getColor(R.color.main_top_color_selected));
                        qzTextView.setTextColor(getResources().getColor((R.color.indictor_selected_text)));
                    }
            }
            currIndex = position;
            animation.setFillAfter(true);// true:图片停在动画结束位置
            animation.setDuration(300);
        //    cursor.startAnimation(animation);

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    ;

    /**
     * 设置动画图片宽度
     *
     * @param mWidth
     */
    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }

    /**
     * 将顶部文字恢复默认值
     */
    private void resetTextViewTextColor() {
        czwTextView.setBackgroundColor(getResources().getColor(R.color.indictor_noselected));
        qzTextView.setBackgroundColor(getResources().getColor(R.color.indictor_noselected));
        czwTextView.setTextColor(getResources().getColor((R.color.indictor_noselected_text)));
       qzTextView.setTextColor(getResources().getColor((R.color.indictor_noselected_text)));
    }

    @Override
    public void onPopupMenuClick(int position) {
        Constants.SAYING_TYPE = position + 1 + "";

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, PublishedActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                if(!dialog.isShowing()){
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }}
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    /**
     * 动作执行
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    if (!dialog.isShowing()) {
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("紧急求救");
                        builder.setMessage("是否发起紧急求救?");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PostSaying();
                            }
                        });
                        dialog = builder.create();
                        dialog.show();
                    }
                    break;
            }
        }

    };

    //获取当前位置
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        //option.setPoiExtraInfo(true);
        if (true) {
            option.setAddrType("all");
        }
        // option.setScanSpan(3000);
        //  option.setPoiNumber(10);
        //option.disableCache(true);
        mLocClient.setLocOption(option);
    }

    //网络请求发表说说
    private void PostSaying() {
        Map map = new HashMap();
        String sayingContent = "我现在在" + strText.getText().toString() + "遇到了紧急情况,需要求助";
        UserUtil.instance(this);

        String userId = UserUtil.getInstance().getIntegerConfig("userId")+"";
        Log.e("userId", userId);
        map.put("userId", userId);
        map.put("postAddress", strText.getText().toString().trim());
        map.put("sayingContent", sayingContent);
        map.put("sayingType", "3");

        String url = Constants.ADD_SAYING;
        VolleyRequest.RequestPost(MainActivity.this, url, "PostSaying", map, new VolleyInterface(MainActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Toast.makeText(MainActivity.this, "已发出急救", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(MainActivity.this, "网络异常，发表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocClient.stop();
    }

    @Override
    public void onDestroy() {
        mLocClient.stop();
        super.onDestroy();
        finish();
    }
}

