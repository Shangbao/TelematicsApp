package com.hangon.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/9.
 */
public class RegetUserActivity extends Activity {
    TextView czUserName;//需要修改密码账户的用户名
    EditText czUserPass1;//重置密码1
    EditText czUserPass2;//重置密码2
    RelativeLayout czUserButton;//重置密码按钮
    private ImageView topbarLeft,topbarRight;//标题栏的左右按钮
    private TextView topbarTitle;//标题栏的中部内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regetuser);
        init();
        getBeforePageValue();
    }

    //获取初始化值 前一个页面传递过来的值
    private void getBeforePageValue(){
        Intent intent=getIntent();
        if(null!=intent){
            String uNameText=intent.getStringExtra("userName");
            czUserName.setText(uNameText);
        }
    }

    //初始化组件
    private void init(){
        czUserName= (TextView) findViewById(R.id.czUserName);
        czUserPass1= (EditText) findViewById(R.id.czUserPass1);
        czUserPass2= (EditText) findViewById(R.id.czUserPass2);
        czUserButton= (RelativeLayout) findViewById(R.id.czUserButton);
        topbarLeft= (ImageView) findViewById(R.id.topbar_left);
        topbarRight= (ImageView) findViewById(R.id.topbar_right);
        topbarTitle= (TextView) findViewById(R.id.topbar_title);
        topbarTitle.setText("重置密码");
        topbarRight.setVisibility(View.GONE);
        //为返回键设置点击事件
        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(RegetUserActivity.this,LoginActivity.class);
                startActivity(intent);
                RegetUserActivity.this.finish();
            }
        });
        //为确定按钮设置点击事件
        czUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUserPass()){
                    judgeUserPass();
                }

            }
        });
    }

    //校验填写内容
    public boolean checkUserPass(){
        String czUserNameText=czUserName.getText().toString();
        String czUserPass1Text=czUserPass1.getText().toString();
        String czUserPass2Text=czUserPass2.getText().toString();
        if(null==czUserNameText||("").equals(czUserNameText)||null==czUserPass1Text||("").equals(czUserPass1Text)||null==czUserPass2Text||("").equals(czUserPass2Text)){
            Toast.makeText(RegetUserActivity.this,"您填写的内容不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(czUserPass1Text.length()<6){
            Toast.makeText(RegetUserActivity.this,"您的密码不能小于六位",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!czUserPass1Text.equals(czUserPass2Text)){
            Toast.makeText(RegetUserActivity.this,"您输入的密码不一致",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //校验用户信息
    private void judgeUserPass(){
        String url= Constants.UPDATE_USERPASS_URL;
        String userName=czUserName.getText().toString();
        String userPass=czUserPass1.getText().toString();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userName",userName);
        map.put("newUserPass",userPass);
        VolleyRequest.RequestPost(RegetUserActivity.this, url, "judgeUser", map, new VolleyInterface(RegetUserActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
            @Override
            public void onMySuccess(String result) {
                Map<String,Object> resultMap= (Map<String, Object>) JsonUtil.jsonToMap(result);
                if(resultMap.get("success").equals(true)){
                    String msg=resultMap.get("msg").toString();
                    Toast.makeText(RegetUserActivity.this,msg,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(RegetUserActivity.this, LoginActivity.class);
                    startActivity(intent);
                    RegetUserActivity.this.finish();
                }else{
                    String msg=resultMap.get("msg").toString();
                    Toast.makeText(RegetUserActivity.this,msg,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMyError(VolleyError error) {

            }
        });
    }
}
