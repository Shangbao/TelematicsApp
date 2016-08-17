package com.hangon.user.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fd.ourapplication.R;

import com.hangon.bean.user.UserInfo;
import com.hangon.common.CleanableEditText;
import com.hangon.common.ConnectionUtil;
import com.hangon.common.Constants;
import com.hangon.common.ImageUtil;
import com.hangon.common.JsonUtil;
import com.hangon.common.MyApplication;
import com.hangon.common.MyStringRequest;
import com.hangon.common.UserUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.home.activity.HomeActivity;
import com.hangon.user.dao.LoginDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2016/3/31.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private CleanableEditText lUserName;//登录时的用户账号
    private CleanableEditText lUserPass;//登录时的用户密码
    private RelativeLayout userLogin;//登录按钮
    private TextView toRegister;//注册按钮
    private TextView toRegetUserPass;//忘记密码
   private Dialog dialog;
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
        lUserName = (CleanableEditText) findViewById(R.id.lUserName);
        lUserPass = (CleanableEditText) findViewById(R.id.lUserPass);
        userLogin = (RelativeLayout) findViewById(R.id.userLogin);
        toRegister = (TextView) findViewById(R.id.userRegister);
        toRegetUserPass= (TextView) findViewById(R.id.userRegetUserPass);
        userLogin.setOnClickListener(this);
        toRegister.setOnClickListener(this);
        toRegetUserPass.setOnClickListener(this);
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
            //显示加载框
            AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
            dialog=new Dialog(LoginActivity.this);
            builder.setView(LayoutInflater.from(LoginActivity.this).inflate(R.layout.actiity_dialog_tip, null));
            dialog=builder.create();
            if (ConnectionUtil.isConn(LoginActivity.this) == false) {
                ConnectionUtil.setNetworkMethod(LoginActivity.this);
            } else {
                login();
            }
        } else {
            lUserName.setText(userInfo.getUserName().trim());
            lUserPass.setText("");
        }
    }


    /**
     * 初始化点击事件按钮的点击事件
     */
    public void onClick(View v) {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        switch (v.getId()) {
            case R.id.userLogin:
                dialog=new Dialog(LoginActivity.this);
                builder.setView(LayoutInflater.from(LoginActivity.this).inflate(R.layout.actiity_dialog_tip, null));
                dialog=builder.create();

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
            case R.id.userRegetUserPass:
                Intent toRegetUpass=new Intent();
                toRegetUpass.setClass(LoginActivity.this,JudgeUserActivity.class);
                startActivity(toRegetUpass);
                this.finish();
        }
    }

    /**
     * 获得登录信息并且解析
     */
    private void login() {
     if(lUserName.getText().toString().trim().equals("")||lUserPass.getText().toString().trim().equals("")){
            Toast.makeText(LoginActivity.this, "账号或密码不能为空.", Toast.LENGTH_SHORT).show();
         return;
        }
       else if (lUserName.length() != 11) {
            Toast.makeText(LoginActivity.this, "账号或者密码错误,请重新输入.", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            dialog.show();
            //url="http://10.163.0.194:8080/wind/UserLogin?userName=13166837709&userPass=123456";
            String url = Constants.LOGIN_URL;
            MyStringRequest request = new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String userInfo) {
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(userInfo);
                        message = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (message.equals("success")) {
                        UserInfo user = new UserInfo();
                        //将用户登录信息的json格式转换成bean对象
                        user = (UserInfo) JsonUtil.jsonToBean(userInfo, UserInfo.class);
                        if(user.getLoginFlag()!=1){
                            //changeLoginFlag(1,user.getUserId());
                            user.setIsSave(true);
                            sendPushTag(user.getUserId()+"");
                            Toast.makeText(LoginActivity.this, "输入正确,正在为你加载中.", Toast.LENGTH_SHORT).show();
                            finishedLogin(user);
                            LoginActivity.this.finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "该用户已登陆,若发现非本人,请修改或找回密码", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Timer timer=new Timer();
                        timer.schedule(new wait(), 2000);
                        Toast.makeText(LoginActivity.this, "账号或者密码错误,请重新输入.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(LoginActivity.this,"网络错误", Toast.LENGTH_SHORT).show();
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
     * 把登陆状态变为登陆状态
     */
    private void changeLoginFlag(int loginFlag,int userId){
     String url=Constants.CHANGE_LOGINFLAG_URL;
     Map<String,Object> map=new HashMap<String,Object>();
     map.put("loginFlag",loginFlag+"");
     map.put("userId",userId+"");
        VolleyRequest.RequestPost(LoginActivity.this, url, "changeLoginFlag", map, new VolleyInterface(LoginActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Map map=new HashMap();
                map=JsonUtil.jsonToMap(result);
                boolean success= (boolean) map.get("success");
                String msg= (String) map.get("msg");
                if(success){
                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
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
            if (s != null && !s.isEmpty()) {
                loadUserIcon(s);
            } else {
                UserUtil.instance(LoginActivity.this);
                UserUtil.getInstance().saveStringConfig("userIconContent", "");
            }
        }
        UserUtil.getInstance().saveLoginUserInfo(userInfo);
        //进行登录页面到主页面的跳转
        sendUserInfo(userInfo);
    }

    /**
     * 从登录Activity到HomeActivity进行值传递
     */
    public void sendUserInfo(UserInfo userInfo) {
        Constants.USER_ID=UserUtil.getInstance().getIntegerConfig("userId");
        Intent toHome = new Intent();
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
        Log.e("userIconConten",userIconConten);
        UserUtil.instance(LoginActivity.this);
        UserUtil.getInstance().saveStringConfig("userIconContent", userIconConten);
    }

    //设置并发送pushtag方法
    private void sendPushTag(String userId){
        Set<String> tagSet = new HashSet<>();
        tagSet.add(userId);
        JPushInterface.setTags(LoginActivity.this, tagSet, new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
                if (responseCode == 0) {
                    Log.d("设置tag结果", responseCode + "");
                } else {
                    Log.d("设置tag错误码", responseCode + "");
                }
            }
        });
    }
    class wait extends TimerTask {

        @Override
        public void run() {
            dialog.dismiss();

        }
    }

}
