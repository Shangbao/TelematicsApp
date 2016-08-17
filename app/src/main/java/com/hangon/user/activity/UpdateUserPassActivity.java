package com.hangon.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.fd.ourapplication.R;
import com.hangon.common.Constants;
import com.hangon.common.JsonUtil;
import com.hangon.common.VolleyInterface;
import com.hangon.common.VolleyRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/9.
 */
public class UpdateUserPassActivity extends Activity {
    TextView czUserName;//用户名
    EditText czUserPass1;//需要修改密码账户的新密码1
    EditText czUserPass2;//需要修改密码账户的新密码2
    Button czUserButton;
    private ImageButton topbarLeft,topbarRight;//标题栏的左右按钮
    private TextView topbarTitle;//标题栏的中部内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgeuser);
        init();
    }

    //初始化组件
    private void init(){
        getUserNameFromBeforePage();
        czUserName= (TextView) findViewById(R.id.czUserName);
        czUserPass1= (EditText) findViewById(R.id.czUserPass1);
        czUserPass2= (EditText) findViewById(R.id.czUserPass2);
        czUserButton= (Button) findViewById(R.id.czUserButton);
        topbarLeft= (ImageButton) findViewById(R.id.topbar_left);
        topbarRight= (ImageButton) findViewById(R.id.topbar_right);
        topbarTitle= (TextView) findViewById(R.id.topbar_title);
        topbarTitle.setText("重置用户密码");
        topbarRight.setVisibility(View.GONE);
        //为返回键设置点击事件
        topbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(UpdateUserPassActivity.this,RegetUserActivity.class);
                startActivity(intent);
                UpdateUserPassActivity.this.finish();
            }
        });
        //为确定按钮设置点击事件
        czUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSubmit()){
                    updateUserPass();
                }
            }
        });
    }

    //获取第一个页面获取的值
    private void getUserNameFromBeforePage(){
        Intent intent=getIntent();
        String userName=intent.getStringExtra("userName");
        czUserName.setText(userName);
    }

    //提交验证
    private boolean checkSubmit(){
        String userPass1=czUserPass1.getText().toString();
        String userPass2=czUserPass2.getText().toString();
        if(null==userPass1||userPass1.equals("")||null==userPass1||userPass1.equals("")){
            Toast.makeText(UpdateUserPassActivity.this,"您输入的密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!userPass1.equals(userPass2)){
            Toast.makeText(UpdateUserPassActivity.this,"您输入的密码不一致,请校验以后重试!",Toast.LENGTH_SHORT).show();
           return false;
        }
        return true;
    }

    //校验用户信息
    private void updateUserPass(){
        String url= Constants.UPDATE_USER_URL;
        String userName=czUserName.getText().toString();
        String userPass=czUserPass2.getText().toString();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userName",userName);
        map.put("newUserPass",userPass);
       VolleyRequest.RequestPost(UpdateUserPassActivity.this, url, "judgeUser", map, new VolleyInterface(UpdateUserPassActivity.this,VolleyInterface.mListener,VolleyInterface.mErrorListener) {
           @Override
           public void onMySuccess(String result) {
               Map<String,Objects> resultMap= (Map<String, Objects>) JsonUtil.jsonToMap(result);
               if(resultMap.get("success").equals(true)){
                   Intent intent=new Intent();
                   intent.setClass(UpdateUserPassActivity.this,LoginActivity.class);
                   startActivity(intent);
                   UpdateUserPassActivity.this.finish();
               }else{
                   String msg=resultMap.get("msg").toString();
                   Toast.makeText(UpdateUserPassActivity.this,msg,Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onMyError(VolleyError error) {

           }
       });
    }
}
