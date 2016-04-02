package com.hangon.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.example.fd.ourapplication.R;
import com.hangon.user.service.LoginListener;
import com.hangon.user.service.RegisterListener;

/**
 * Created by Administrator on 2016/3/31.
 */
public class LoginActivity extends Activity {
    private  EditText username;
    private EditText userPass;
    private Button userLogin;
    private Button userRegister;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        username= (EditText) findViewById(R.id.username);
        userPass= (EditText) findViewById(R.id.userPass);
        userLogin= (Button) findViewById(R.id.userLogin);
        userRegister= (Button) findViewById(R.id.userRegister);
        userLogin.setOnClickListener(new LoginListener());
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegister = new Intent();
                toRegister.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
            }
        });
    }

    public  View getView(View view){
        view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.activity_login,null);
        return view;
    }

    public EditText getUsername() {
        return username;
    }

    public void setUsername(EditText username) {
        this.username = username;
    }

    public EditText getUserPass() {
        return userPass;
    }

    public void setUserPass(EditText userPass) {
        this.userPass = userPass;
    }
}
