package com.hangon.push;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.JsonUtil;
import com.hangon.common.MyApplication;
import com.hangon.common.Topbar;
import com.hangon.map.activity.MapMainActivity;
import com.hangon.map.util.JudgeNet;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Chuan on 2016/8/10.
 */
public class PushActivity extends Activity implements View.OnClickListener {

    Intent intent;
    Bundle bundle;
    JudgeNet judge;

    private String title;
    private String alert;
    private String extra;           //额外消息，这是一个json字符串
    private String address;             //求助人地址
    private String lat;             //求助人纬度
    private String lng;             //求助人经度
    private String username;       //求助人手机号码
    private String nickName;       //求助人昵称
    private String userIconUrl;   //求助人头像
    private String content;        //求助人求助内容

   ImageView leftTopbar,rightTopbar;//标题栏的左右按钮
   TextView titleTopbar;//标题栏的内容

   ImageView jjqzUserIcon;//求助人用户头像
   TextView jjqzNickName;//求助人用户昵称
   TextView jjqzContent;//求助人发表内容
   TextView jjqzAddress;//求助人地址
    TextView jjqzPhoneNum;//求助人电话号码

    ImageView jjqzDzq;//紧急求助到这去
    ImageView jjqzLxfs;//紧急求助联系方式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        initPushMessage();
        init();
        getUserIconBitmap();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jjqz_dzq:
            judge = new JudgeNet();
            judge.setStates(1);
            judge.setAppointRoute(1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putDouble("lon", Double.parseDouble(lng));
                    bundle.putDouble("lat", Double.parseDouble(lat));
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MapMainActivity.class);
                    intent.putExtra("endaddress", bundle);
                    startActivity(intent);
                }
            }).start();
            break;

            case R.id.jjqz_phoneNum:

                break;

            case R.id.topbar_left:
                this.finish();
                break;
        }
    }

    //组件初始化
    private void init(){
        leftTopbar= (ImageView) findViewById(R.id.topbar_left);
        rightTopbar= (ImageView) findViewById(R.id.topbar_right);
        titleTopbar= (TextView) findViewById(R.id.topbar_title);
        jjqzUserIcon= (ImageView) findViewById(R.id.jjqz_userIcon);
        jjqzNickName= (TextView) findViewById(R.id.jjqz_userNickname);
        jjqzContent= (TextView) findViewById(R.id.jjqz_content);
        jjqzAddress= (TextView) findViewById(R.id.jjqz_address);
        jjqzDzq= (ImageView) findViewById(R.id.jjqz_dzq);
        jjqzPhoneNum= (TextView) findViewById(R.id.jjqz_phoneNum);
        jjqzLxfs= (ImageView) findViewById(R.id.jjqz_btnLXfs);

        jjqzDzq.setOnClickListener(this);
        jjqzLxfs.setOnClickListener(this);
        rightTopbar.setVisibility(View.GONE);
        leftTopbar.setOnClickListener(this);
        titleTopbar.setText("紧急求助");

        jjqzNickName.setText("求助用户:" + nickName);
        jjqzAddress.setText(address);
        jjqzPhoneNum.setText(username);
        jjqzContent.setText(content);
        jjqzAddress.setText(address);
    }

    //初始化获取到的位置
    private void initPushMessage(){
        intent = getIntent();
        bundle = intent.getExtras();
        title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

        Map map = JsonUtil.jsonToMap(extra);
        address = (String) map.get("qzrPostAddress");
        lat = (String) map.get("qzrLat");
        lng = (String) map.get("qzrLng");
        username = (String) map.get("qzrUserName");
        nickName = (String) map.get("qzrNickName");
        userIconUrl = (String) map.get("qzrUserIconUrl");
        content = (String) map.get("qzrPostContent");
        Toast.makeText(PushActivity.this, "esxtr:"+extra, Toast.LENGTH_SHORT).show();
        Toast.makeText(PushActivity.this, "0000000", Toast.LENGTH_SHORT).show();
      //  Toast.makeText(PushActivity.this, json, Toast.LENGTH_SHORT).show();
    }

    //转化头像
    private void getUserIconBitmap() {
        String url = Constants.LOAD_USER_ICON_URL + userIconUrl;
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                jjqzUserIcon.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PushActivity.this, "求助人头像加载失败,请重试!", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getHttpQueues().add(request);
    }
}
