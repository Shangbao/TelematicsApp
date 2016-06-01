package com.hangon.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fd.ourapplication.R;

import com.hangon.bean.user.UserInfo;
import com.hangon.common.ConnectionUtil;
import com.hangon.common.Constants;
import com.hangon.common.ImageUtil;
import com.hangon.common.JsonUtil;
import com.hangon.common.MyApplication;
import com.hangon.common.MyStringRequest;
import com.hangon.common.UserUtil;
import com.hangon.home.activity.HomeActivity;
import com.hangon.user.dao.LoginDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/31.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText lUserName;//登录时的用户账号
    private EditText lUserPass;//登录时的用户密码
    private Button userLogin;//登录按钮
    private Button toRegister;//注册按钮

    public static String autoLogin;//判断是否自动登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        autoLogin();
    }

    /**
     * 初始化组件和一些值和对象
     */
    private void init() {
        lUserName = (EditText) findViewById(R.id.lUserName);
        lUserPass = (EditText) findViewById(R.id.lUserPass);
        userLogin = (Button) findViewById(R.id.userLogin);
        toRegister = (Button) findViewById(R.id.userRegister);
        userLogin.setOnClickListener(this);
        toRegister.setOnClickListener(this);
        //autoLogin=false;
        UserUtil.instance(LoginActivity.this);
    }

    /**
     * 取出保存在文件里的信息并进行自动登录
     */
    public void autoLogin() {
        UserInfo userInfo = UserUtil.getInstance().getUserInfo4Login();
        if (userInfo.isSave()) {
            lUserName.setText(userInfo.getUserName().trim());
            lUserPass.setText(userInfo.getUserPass().trim());
            sendUserInfo(userInfo);
            this.finish();
        } else {
            lUserName.setText(userInfo.getUserName().trim());
            lUserPass.setText("");
        }
    }


    /**
     * 初始化点击事件按钮的点击事件
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userLogin:
                if (ConnectionUtil.isConn(LoginActivity.this) == false) {
                    ConnectionUtil.setNetworkMethod(LoginActivity.this);
                } else {
                    login();
                }
                break;
            case R.id.userRegister:
                Intent toRegister = new Intent();
                toRegister.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
                break;
        }
    }

    /**
     * 获得登录信息并且解析
     */
    private void login() {
        if (lUserName.length() != 11) {
            Toast.makeText(LoginActivity.this, "账号或者密码错误,请重新输入.", Toast.LENGTH_SHORT).show();
        } else {
            //url="http://10.163.0.194:8080/wind/UserLogin?userName=13166837709&userPass=123456";
            String url = Constants.LOGIN_URL;
            Log.e("yyy", url);
            MyStringRequest request = new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String userInfo) {
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(userInfo);
                        message = jsonObject.getString("message");
                        Log.e("yyy", message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (message.equals("success")) {
                        UserInfo user = new UserInfo();
                        //将用户登录信息的json格式转换成bean对象
                        user = (UserInfo) JsonUtil.jsonToBean(userInfo, UserInfo.class);
                        user.setIsSave(true);
                        Toast.makeText(LoginActivity.this, "输入正确,正在为你加载中.", Toast.LENGTH_SHORT).show();
                        finishedLogin(user);
                        LoginActivity.this.finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "账号或者密码错误,请重新输入.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("xxx", userInfo);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userName", lUserName.getText().toString());
                    map.put("userPass", lUserPass.getText().toString());
                    return map;
                }
            };
            request.setTag("StringReqGet");
            MyApplication.getHttpQueues().add(request);
        }
    }

    /**
     * 完成登录
     */
    private void finishedLogin(UserInfo userInfo) {
        //设置自动登录状态
        userInfo.setIsSave(true);
        //保存用户信息到文件里
        if (!userInfo.getUserName().equals(UserUtil.getInstance().getStringConfig("userName"))) {
            String s = userInfo.getUserIconUrl().trim();
            Log.e("aa1", "111111");
            if (s != null && !s.isEmpty()) {
                Log.e("aa2", "222222");
                loadUserIcon(s);
            } else {
                Log.e("aa3", "333333");
                UserUtil.instance(LoginActivity.this);
                UserUtil.getInstance().saveStringConfig("userIconContent", "");
            }
        }
        UserUtil.getInstance().saveLoginUserInfo(userInfo);
        Log.e("uuuuuu", UserUtil.getInstance().getIntegerConfig("userId")+"");
        //进行登录页面到主页面的跳转
        sendUserInfo(userInfo);
    }

    /**
     * 从登录Activity到HomeActivity进行值传递
     */
    public void sendUserInfo(UserInfo userInfo) {
        Intent toHome = new Intent();
        //装载数据
        Bundle bundle = new Bundle();
        bundle.putString("userName", userInfo.getUserName());
        bundle.putString("nickname", userInfo.getNickname());
        bundle.putString("sex", userInfo.getSex());
        bundle.putInt("age", userInfo.getAge());
        bundle.putString("driverNum", userInfo.getDriverNum());
        toHome.putExtra("bundle", bundle);
        toHome.setClass(LoginActivity.this, HomeActivity.class);
        startActivity(toHome);
    }

    //   //读取图片并且加载链接
    private void loadUserIcon(String s) {
        String url = Constants.LOAD_USER_ICON_URL + s;
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                //把获取的图片保存到cookies里面
                saveIconToCookies(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "用户头像获取失败", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getHttpQueues().add(request);
    }

    //把获取的图片保存到cookies里面
    private void saveIconToCookies(Bitmap bitmap) {
        byte[] bytes;
        bytes = ImageUtil.getBitmapByte(bitmap);
        String userIconConten = ImageUtil.getStringFromByte(bytes);
        UserUtil.instance(LoginActivity.this);
        UserUtil.getInstance().saveStringConfig("userIconContent", userIconConten);
    }

}
