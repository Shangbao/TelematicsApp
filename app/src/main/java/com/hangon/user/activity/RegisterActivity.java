package com.hangon.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.test.LoaderTestCase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.MyApplication;
import com.hangon.common.Topbar;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;


/**
 * Created by Administrator on 2016/3/31.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText rUserName;
    private EditText cord;
    private TextView now;
    private Button getCord;
    private Button saveCord;
    private EditText rUserPass;
    private Topbar registerTopbar;

    private String iPhone;
    private String iCord;
    private int time = 60;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        init();
        SMSSDK.initSDK(this, "113728ac0f8dd", "32ca2176e3fa1f3b4eedadbd74de1ee6");
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);

    }

    private void init() {
        rUserName = (EditText) findViewById(R.id.rUserName);
        cord = (EditText) findViewById(R.id.cord);
        now = (TextView) findViewById(R.id.now);
        getCord = (Button) findViewById(R.id.getCord);

        saveCord = (Button) findViewById(R.id.saveCord);
        rUserPass = (EditText) findViewById(R.id.rUserPass);
        registerTopbar = (Topbar) findViewById(R.id.registerTopbar);
        getCord.setOnClickListener(this);
        saveCord.setOnClickListener(this);

        registerTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                RegisterActivity.this.finish();
            }

            @Override
            public void rightClick() {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //获取验证码
            case R.id.getCord:
                if (!TextUtils.isEmpty(rUserName.getText().toString().trim())) {
                    if (rUserName.getText().toString().trim().length() == 11) {
                        judgeUserExist();

                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        rUserName.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    rUserName.requestFocus();
                }
                break;
            //保存验证码
            case R.id.saveCord:
                if (!TextUtils.isEmpty(cord.getText().toString().trim())) {
                    if (cord.getText().toString().trim().length() == 4) {
                        iCord = cord.getText().toString().trim();
                        flag = false;
                        if (!TextUtils.isEmpty(rUserPass.getText().toString().trim())) {
                            if (rUserPass.getText().toString().trim().length() < 6 || rUserPass.getText().toString().trim().length() > 12) {
                                Toast.makeText(RegisterActivity.this, "密码需在6-12位之间！", Toast.LENGTH_LONG).show();
                                rUserPass.requestFocus();
                            } else {
                                SMSSDK.submitVerificationCode("86", iPhone, iCord);
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                            rUserPass.requestFocus();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        cord.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    cord.requestFocus();
                }
                break;
        }
    }

    //验证码送成功后提示文字
    private void reminderText() {
        now.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    Handler handlerText = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (time > 0) {
                    now.setText("验证码已发送" + time + "秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                } else {
                    getCord.setText("重新发送");

                    time = 60;
                    now.setVisibility(View.GONE);
                    getCord.setVisibility(View.VISIBLE);
                }
            } else {
                cord.setText("");

                getCord.setText("重新发送");
                time = 60;
                now.setVisibility(View.GONE);
                getCord.setVisibility(View.VISIBLE);
            }
        }
    };

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    handlerText.sendEmptyMessage(2);
                    addUserInfo();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (flag) {
                    getCord.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    rUserName.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    cord.selectAll();
                    if (resId > 0) {
                        Toast.makeText(RegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }

    };

    //添加用户
    private void addUserInfo() {
        String url = Constants.REGISTER_URL;
        Map<String, Object> map = new HashMap<>();
        map.put("userName", rUserName.getText().toString());
        map.put("userPass", rUserPass.getText().toString());
        VolleyRequest.RequestPost(RegisterActivity.this, url, "postUserInfo", map, new VolleyInterface(RegisterActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("aaa", result);
                if (result.equals("OK")) {
                    Toast.makeText(RegisterActivity.this, "注册成功。", Toast.LENGTH_SHORT).show();
                    Intent toLogin = new Intent();
                    toLogin.setClass(RegisterActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                    finish();
                }

            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT);
            }
        });
    }

    //判断用户名是否存在
    private void judgeUserExist() {
        String url = Constants.JUDGE_USER_URL + "userName=" + rUserName.getText();
        VolleyRequest.RequestGet(RegisterActivity.this, url, "userGet", new VolleyInterface(RegisterActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                if (result.equals("error")) {
                    Toast.makeText(RegisterActivity.this, "账号已存在，请重新输入", Toast.LENGTH_SHORT).show();
                    rUserName.setText("");
                    rUserPass.setText("");
                    cord.setText("");
                } else if (result.equals("success")) {
                    iPhone = rUserName.getText().toString().trim();
                    SMSSDK.getVerificationCode("86", iPhone);
                    cord.requestFocus();
                    getCord.setVisibility(View.GONE);
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
