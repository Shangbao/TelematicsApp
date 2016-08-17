package com.hangon.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.JsonUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;
import com.hangon.fragment.userinfo.UpdateUserActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/9.
 */
public class JudgeUserActivity extends Activity {
    EditText jUserName;//需要修改密码账户的用户名
    EditText codeEditText;//需要修改密码账户的密码
    RelativeLayout nextPage;//下一步
    Button getEditTextCord;//获取验证码
    private ImageView topbarLeft,topbarRight;//标题栏的左右按钮
    private TextView topbarTitle;//标题栏的中部内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgeuser);
        init();
    }

    //初始化组件
    private void init(){
        jUserName= (EditText) findViewById(R.id.jUserName);
        codeEditText= (EditText) findViewById(R.id.codeEditText);
        getEditTextCord= (Button) findViewById(R.id.getEditTextCord);
        nextPage= (RelativeLayout) findViewById(R.id.nextPage);
        topbarLeft= (ImageView) findViewById(R.id.topbar_left);
        topbarRight= (ImageView) findViewById(R.id.topbar_right);
        topbarTitle= (TextView) findViewById(R.id.topbar_title);
        topbarTitle.setText("校验用户");
        topbarRight.setVisibility(View.GONE);
        //为返回键设置点击事件
        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(JudgeUserActivity.this, LoginActivity.class);
                startActivity(intent);
                JudgeUserActivity.this.finish();
            }
        });
        //获取验证码
        getEditTextCord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JudgeUserActivity.this,"你的验证码为:666888",Toast.LENGTH_SHORT).show();
            }
        });
        //为确定按钮设置点击事件
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSubmit()) {
                    judgeUser();
                }
            }
        });
    }

    //校验提交
    private boolean checkSubmit(){
        String userName=jUserName.getText().toString();
        String checkCode=codeEditText.getText().toString();
        if(null==userName||userName.equals("")||null==checkCode||checkCode.equals("")){
            Toast.makeText(JudgeUserActivity.this,"你输入的用户名或者验证码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(userName.length()!=11){
            Toast.makeText(JudgeUserActivity.this,"用户名为11位数字",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!checkCode.equals("666888")){
            Toast.makeText(JudgeUserActivity.this,"验证码为:666888",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //校验用户信息
    private void judgeUser(){
        final String userName=jUserName.getText().toString();
        String url= Constants.JUDGE_USER_URL+"userName="+userName;
        VolleyRequest.RequestGet(JudgeUserActivity.this, url, "judgeUser", new VolleyInterface(JudgeUserActivity.this, VolleyInterface.mListener, VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Log.e("result",result);
                if (result.equals("error")) {
                    //说明用户存在
                    Intent intent = new Intent();
                    intent.putExtra("userName", userName);
                    intent.setClass(JudgeUserActivity.this, RegetUserActivity.class);
                    JudgeUserActivity.this.startActivity(intent);
                    JudgeUserActivity.this.finish();
                }else{
                    Toast.makeText(JudgeUserActivity.this,"该用户不存在",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }
}
